package dsp;

import java.util.List;

/**
 * @author niktrk
 * 
 */
public abstract class ThresholdEndPointDetection {

	private List<Frame> frames;
	private double[] frameValue;
	private double threshold;

	public ThresholdEndPointDetection(List<Frame> frames, boolean skipZerosInMinCalculation) {
		super();
		this.frames = frames;
		frameValue = new double[frames.size()];
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i = 0; i < frames.size(); i++) {
			frameValue[i] = calculateFrameValue(frames.get(i));
			if (!skipZerosInMinCalculation || Math.abs(frameValue[i]) > Constants.EPS) {
				min = Math.min(min, frameValue[i]);
			}
			max = Math.max(max, frameValue[i]);
		}
		threshold = calculateThreshold(min, max);
	}

	public List<Frame> removeStartAndEnd() {
		int startIndex = 0;
		for (startIndex = 0; startIndex < frames.size(); startIndex++) {
			if (frameValue[startIndex] >= threshold) {
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
			if (frameValue[endIndex] >= threshold) {
				int previousOffset = previousBelowThresholdOffset(endIndex);
				if (previousOffset == -1) {
					break;
				} else {
					endIndex -= previousOffset;
				}
			}
		}
		return frames.subList(Math.max(0, startIndex - getEndPointDetectionOffset()),
				Math.min(endIndex + 1 + getEndPointDetectionOffset(), frames.size()));
	}

	private int nextBelowThresholdOffset(int startIndex) {
		for (int i = startIndex + 1; i < frames.size() && i < startIndex + getMinConsecutiveSpeechFrames(); i++) {
			if (frameValue[i] < threshold) {
				return i - startIndex;
			}
		}
		return -1;
	}

	private int previousBelowThresholdOffset(int startIndex) {
		for (int i = startIndex - 1; i >= 0 && i > startIndex - getMinConsecutiveSpeechFrames(); i--) {
			if (frameValue[i] < threshold) {
				return startIndex - i;
			}
		}
		return -1;
	}

	protected abstract double calculateFrameValue(Frame frame);

	protected abstract double calculateThreshold(double min, double max);

	protected abstract int getMinConsecutiveSpeechFrames();

	protected abstract int getEndPointDetectionOffset();

}
