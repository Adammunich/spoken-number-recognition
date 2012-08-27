package dsp.endpointdetection;

import java.util.List;

import dsp.model.Frame;

/**
 * @author niktrk
 * 
 */
public class EnergyEndPointDetection extends ThresholdEndPointDetection {

	public EnergyEndPointDetection(List<Frame> frames) {
		super(frames, true);
	}

	@Override
	protected double calculateFrameValue(Frame frame) {
		double energy = 0d;
		for (int i = 0; i < frame.getOriginalBuffer().length; i++) {
			energy += frame.getOriginalBuffer()[i] * frame.getOriginalBuffer()[i];
		}
		return Math.sqrt(energy / frame.getOriginalBuffer().length);
	}

	@Override
	protected double calculateThreshold(double min, double max) {
		double lambda = (max - min) / max;
		return (1 - lambda) * max + lambda * min;
	}

	@Override
	protected int getMinConsecutiveSpeechFrames() {
		return 7;
	}

	@Override
	protected int getEndPointDetectionOffset() {
		return 2;
	}

}
