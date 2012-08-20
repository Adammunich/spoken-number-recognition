package dsp;

/**
 * @author niktrk
 * 
 */
public class WeightedMFCC implements Transformation {

	public WeightedMFCC() {
		super();
	}

	@Override
	public double[] transform(double[] input) {
		double[] result = new double[Constants.MFCC_LENGTH];
		for (int i = 0; i < result.length; i++) {
			result[i] = input[i];
			result[i] += Constants.MFCC_WIGHT_FACTOR_P * input[i + Constants.MFCC_LENGTH];
			result[i] += Constants.MFCC_WIGHT_FACTOR_Q * input[i + 2 * Constants.MFCC_LENGTH];
		}
		return result;
	}

}
