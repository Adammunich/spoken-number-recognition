package dsp;

/**
 * @author niktrk
 * 
 */
public class PreEmphasisFilter implements Transformation {

	private static final double ALPHA = 0.97d;

	public PreEmphasisFilter() {
		super();
	}

	@Override
	public double[] transform(double[] input) {
		double[] result = new double[input.length];
		result[0] = input[0];
		for (int i = 1; i < result.length; i++) {
			result[i] = input[i] - ALPHA * input[i - 1];
		}
		return result;
	}

}
