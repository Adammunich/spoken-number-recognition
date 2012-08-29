package som;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class Input {

	private final double[] values;
	private final String inputName;

	public Input(double[] values, String inputName) {
		super();
		this.values = values;
		this.inputName = inputName;
		normalize();
	}

	public double[] getValues() {
		return values;
	}

	public String getInputName() {
		return inputName;
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
