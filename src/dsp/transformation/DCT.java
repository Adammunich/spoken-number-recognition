package dsp.transformation;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class DCT implements Transformation {

	public DCT() {
		super();
	}

	@Override
	public double[] transform(double[] input) {
		double[] result = new double[input.length];

		for (int i = 0; i < result.length; i++) {
			double value = 0d;
			double factor = Math.PI * i / input.length;
			for (int j = 0; j < input.length; j++) {
				value += input[j] * Math.cos(factor * (j + 0.5d));
			}
			result[i] = value;
		}

		return result;
	}

}
