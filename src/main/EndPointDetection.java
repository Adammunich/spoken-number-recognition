package main;

import java.util.List;

/**
 * @author niktrk
 * 
 */
// TODO add threshold for frequencies, probability density and remove short
// speech segments
public class EndPointDetection {

	private static final double OFFSET_FACTOR = 1d;

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
			spectralEntropy[i] = frames.get(i).calculateSpectralEntropy();
			min = Math.min(min, spectralEntropy[i]);
			max = Math.max(max, spectralEntropy[i]);
		}
		threshold = (max - min) / 2 + OFFSET_FACTOR * min;
		System.out.println(min + " " + max + " " + threshold);
	}

	public List<Frame> removeStartAndEnd() {
		int startIndex = 0;
		for (startIndex = 0; startIndex < frames.size(); startIndex++) {
			if (spectralEntropy[startIndex] >= threshold) {
				break;
			}
		}
		int endIndex = 0;
		for (endIndex = frames.size() - 1; endIndex >= 0; endIndex--) {
			if (spectralEntropy[endIndex] >= threshold) {
				break;
			}
		}
		return frames.subList(startIndex, endIndex + 1);
	}

}
