package dsp;

/**
 * @author niktrk
 * 
 */
public class WeightedMFCC implements Transformation {

	private final double p = 1d / 3d;
	private final double q = 1d / 6d;

	public WeightedMFCC() {
		super();
	}

	@Override
	public double[] transform(double[] input) {
		int mfccLength = input.length / 3;
		double[] result = new double[mfccLength];
		for (int i = 0; i < result.length; i++) {
			result[i] = input[i];
			result[i] += p * input[i + mfccLength];
			result[i] += q * input[i + mfccLength + mfccLength];
		}
		return result;
	}

}
