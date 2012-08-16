package som;

import java.util.Random;

public class TrainingSet {

	private Input[] inputs;

	public TrainingSet(Input[] inputs) {
		super();
		this.inputs = inputs;
	}

	public Input[] getInputs() {
		return inputs;
	}

	public void setInputs(Input[] inputs) {
		this.inputs = inputs;
	}

	public Input getRandomInput() {
		Random r = new Random();
		int index = r.nextInt(inputs.length - 1);
		System.out.println();
		System.out.println("input no " + index);
		return inputs[index];
	}

}
