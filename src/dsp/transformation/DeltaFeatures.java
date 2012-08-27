package dsp.transformation;


/**
 * @author niktrk
 * 
 */
public class DeltaFeatures implements Transformation {

	private int depth;
	private double inverseSquareSum;

	public DeltaFeatures(int depth) {
		super();
		this.depth = depth;
		for (int i = 1; i <= depth; i++) {
			inverseSquareSum += i * i;
		}
		inverseSquareSum = 1 / inverseSquareSum;// *2?
	}

	@Override
	public double[] transform(double[] input) {
		double[] result = new double[input.length * 3];

		double[] delta = calculateDeltaFeatures(input);
		double[] doubleDelta = calculateDeltaFeatures(delta);

		System.arraycopy(input, 0, result, 0, input.length);
		System.arraycopy(delta, 0, result, input.length, delta.length);
		System.arraycopy(doubleDelta, 0, result, input.length + delta.length, doubleDelta.length);

		return result;
	}

	private double[] calculateDeltaFeatures(double[] input) {
		double[] result = new double[input.length];
		double sum, subSum;
		for (int i = 0; i < result.length; i++) {
			result[i] = inverseSquareSum;
			sum = 0d;
			for (int j = 1; j <= depth; j++) {
				subSum = 0d;
				if (i + j < input.length) {
					subSum = input[i + j];
				}
				if (i - j >= 0) {
					subSum -= input[i - j];
				}
				subSum *= j;
				sum += subSum;
			}
			result[i] *= sum;
		}
		return result;
	}

}
