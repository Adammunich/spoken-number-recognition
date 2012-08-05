package main;

import java.util.Random;

public class TrainingSet {

	private Input[] inputs;

	public Input[] getInputs() {
		return inputs;
	}

	public void setInputs(Input[] inputs) {
		this.inputs = inputs;
	}

	public Input getRandomInput() {
		Random r = new Random();
		int index = r.nextInt(inputs.length - 1);
		return inputs[index];
	}

}
