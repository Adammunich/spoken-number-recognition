package som;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TrainingSet {

	private static final Random random = new Random();
	private Input[] inputs;

	private List<Integer> indices = new ArrayList<Integer>(4000);

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

	public List<Integer> getList() {
		Collections.reverse(indices);
		return indices;
	}

	public Input getRandomInput() {
		int index = random.nextInt(inputs.length);
		indices.add(index);
		// System.out.println();
		// System.out.println("input no " + index);
		return inputs[index];
	}

}
