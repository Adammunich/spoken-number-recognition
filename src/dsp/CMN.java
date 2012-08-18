package dsp;

import java.util.List;

/**
 * @author niktrk
 * 
 */
public class CMN implements Transformation {

	private double[] averageVector;

	public CMN(List<Frame> frames) {
		super();
		averageVector = new double[13];
		for (Frame frame : frames) {
			for (int i = 0; i < frame.getBuffer().length; i++) {
				averageVector[i] += frame.getBuffer()[i];
			}
		}
		for (int i = 0; i < averageVector.length; i++) {
			averageVector[i] /= frames.size();
		}
	}

	@Override
	public double[] transform(double[] input) {
		double[] result = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			result[i] = input[i] - averageVector[i];
		}
		return result;
	}

}
