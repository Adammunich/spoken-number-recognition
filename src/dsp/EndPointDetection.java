package dsp;

import java.util.List;

/**
 * @author niktrk
 * 
 */
// TODO add threshold for frequencies, probability density
public class EndPointDetection {

	private static final double OFFSET_FACTOR = 1d;
	private static final int MIN_CONSECUTIVE_SPEECH_FRAMES = 2;

	private List<Frame> frames;
	private double[] spectralEntropy;
	private double min;
	private double max;
	private double threshold;

	public EndPointDetection(List<Frame> frames) {
		super();
		this.frames = frames;
		spectralEntropy = new double[frames.size()];
		min = Double.MAX_VALUE;
		max = Double.MIN_VALUE;
		for (int i = 0; i < frames.size(); i++) {
			spectralEntropy[i] = calculateSpectralEntropy(frames.get(i));
			System.out.println("SpectralEntropy " + spectralEntropy[i]);
			min = Math.min(min, spectralEntropy[i]);
			max = Math.max(max, spectralEntropy[i]);
		}
		threshold = (max - min) / 2 + OFFSET_FACTOR * min;
		System.out.println(min + " " + max + " " + threshold);
	}

	private double calculateSpectralEntropy(Frame frame) {
		// total energy
		double totalSpectralEnergy = 0d;
		for (int i = 0; i < frame.getBuffer().length / 2; i++) {
			totalSpectralEnergy += frame.getBuffer()[i];
		}
		// probability
		double[] probabilityDensity = new double[frame.getBuffer().length / 2];
		for (int i = 0; i < probabilityDensity.length; i++) {
			if (totalSpectralEnergy < 1e-9) {
				probabilityDensity[i] = 0d;
			} else {
				probabilityDensity[i] = frame.getBuffer()[i] / totalSpectralEnergy;
			}
		}
		// entropy
		double spectralEntropy = 0d;
		for (int i = 0; i < probabilityDensity.length; i++) {
			if (probabilityDensity[i] > 1e-9) {
				spectralEntropy += probabilityDensity[i] * Math.log(probabilityDensity[i]);
			}
		}
		return -spectralEntropy;
	}

	public List<Frame> removeStartAndEnd() {
		int startIndex = 0;
		for (startIndex = 0; startIndex < frames.size(); startIndex++) {
			if (spectralEntropy[startIndex] >= threshold) {
				// break;
				int nextOffset = nextBelowThresholdOffset(startIndex);
				if (nextOffset == -1) {
					break;
				} else {
					startIndex += nextOffset;
				}
			}
		}
		int endIndex = 0;
		for (endIndex = frames.size() - 1; endIndex >= 0; endIndex--) {
			if (spectralEntropy[endIndex] >= threshold) {
				// break;
				int previousOffset = previousBelowThresholdOffset(endIndex);
				if (previousOffset == -1) {
					break;
				} else {
					endIndex -= previousOffset;
				}
			}
		}
		return frames.subList(startIndex, endIndex + 1);
	}

	private int nextBelowThresholdOffset(int startIndex) {
		for (int i = startIndex + 1; i < frames.size() && i < startIndex + MIN_CONSECUTIVE_SPEECH_FRAMES; i++) {
			if (spectralEntropy[i] < threshold) {
				return i - startIndex;
			}
		}
		return -1;
	}

	private int previousBelowThresholdOffset(int startIndex) {
		for (int i = startIndex - 1; i >= 0 && i > startIndex - MIN_CONSECUTIVE_SPEECH_FRAMES; i--) {
			if (spectralEntropy[i] < threshold) {
				return startIndex - i;
			}
		}
		return -1;
	}

}
