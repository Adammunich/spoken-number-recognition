package som;

public class Input {

	private double[] values;

	public Input(double[] values) {
		this.values = values;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public void normalize() {
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
