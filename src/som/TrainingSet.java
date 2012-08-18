package som;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainingSet {

	private List<Integer> indices = new ArrayList<Integer>(4000);
	Random r = new Random();

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

	public void printMe() {
		Collections.reverse(indices);
		System.out.print("Input ");
		System.out.println(indices);
	}

	public Input getRandomInput() {
		int index = r.nextInt(inputs.length - 1);
		indices.add(index);
		System.out.println();
		System.out.println("input no " + index);
		return inputs[index];
	}

}
