package main;

import java.util.Random;

public class SOM {
	private int numInput;
	private int numOutput;
	private double[][] weights;
	private double startLearningRate;
	private int numIteration;
	private int countIteration;

	public SOM(int numInput, int numOutput) {
		this.numInput = numInput;
		this.numOutput = numOutput;
		this.weights = new double[numOutput][numInput];
		for (int i = 0; i < numOutput; i++) {
			for (int j = 0; j < numInput; j++) {
				weights[i][j] = 0;
			}
		}
	}

	public int getNumInput() {
		return numInput;
	}

	public void setNumInput(int numInput) {
		this.numInput = numInput;
	}

	public int getNumOutput() {
		return numOutput;
	}

	public void setNumOutput(int numOutput) {
		this.numOutput = numOutput;
	}

	public void train(TrainingSet ts, int numIteration) {
		initWeights();
		this.numIteration = numIteration;
		countIteration = 0;
		boolean ok = true;
		while (countIteration < numIteration && ok) {
			ok = epoch(ts);
			countIteration++;
		}
	}

	public int findWinner(Input input) {
		double[] values = input.getValues();
		if (values.length != numInput)
			return -1;
		else {
			int winner = 0;
			double min = Double.MAX_VALUE;
			double currentDist = 0;
			for (int i = 0; i < numOutput; i++) {
				currentDist = getDistance(weights[i], values);
				if (currentDist < min) {
					min = currentDist;
					winner = i;
				}
			}
			return winner;
		}
	}
	
	private boolean epoch(TrainingSet ts) {
		Input input = ts.getRandomInput();
		input.normalize();
		if (input.getValues().length != numInput) {
			return false;
		} 
		else {
			int winner = findWinner(input);
			adjustWeights(input, winner);
			return true;
		}
	}

	private void initWeights() {
		Random r = new Random();
		for (int i = 0; i < numOutput; i++) {
			for (int j = 0; j < numInput; j++) {
				weights[i][j] = r.nextDouble() / 3; // small random number
			}
		}
	}

	private void adjustWeights(Input input, int winner) {
		double [] values = input.getValues();
		for (int i = 0; i < numInput; i++) {
			weights[winner][i] += getLearningRate()
					* (values[i] - weights[winner][i]);
		}
	}

	private double getLearningRate() {
		return startLearningRate * Math.exp(-numIteration / countIteration);
	}

	private double getDistance(double[] a, double[] b) {
		int dist = 0;
		for (int i = 0; i < a.length; i++) {
			dist += Math.pow(a[i] - b[i], 2);
		}
		return Math.sqrt(dist);
	}

}
