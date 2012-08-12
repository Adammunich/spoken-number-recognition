package dsp;

/**
 * @author niktrk
 * 
 */
public class HammingWindow implements Transformation {

	public HammingWindow() {
		super();
	}

	@Override
	public double[] transform(double[] input) {
		double[] result = new double[input.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = input[i] * (0.54 - 0.46 * Math.cos(2 * Math.PI * i / (input.length - 1)));
		}

		return result;
	}

}
