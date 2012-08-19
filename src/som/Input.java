package som;

public class Input {

	private final double[] values;

	public Input(double[] values) {
		super();
		this.values = values;
		normalize();
	}

	public double[] getValues() {
		return values;
	}

	private void normalize() {
		double sum = 0;
		for (int i = 0; i < values.length; i++) {
			sum += values[i] * values[i];
		}
		sum = Math.sqrt(sum);
		if (sum != 0) {
			for (int i = 0; i < values.length; i++) {
				values[i] /= sum;
			}
		}
	}

}
