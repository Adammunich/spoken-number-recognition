package dsp.transformation;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class WeightedMFCC implements Transformation {

	private static final double WEIGHT_FACTOR_P = 1 / 3d;
	private static final double WEIGHT_FACTOR_Q = 1 / 6d;

	public WeightedMFCC() {
		super();
	}

	@Override
	public double[] transform(double[] input) {
		int length = input.length / 3;
		double[] result = new double[length];
		for (int i = 0; i < result.length; i++) {
			result[i] = input[i];
			result[i] += WEIGHT_FACTOR_P * input[i + length];
			result[i] += WEIGHT_FACTOR_Q * input[i + 2 * length];
		}
		return result;
	}

}
