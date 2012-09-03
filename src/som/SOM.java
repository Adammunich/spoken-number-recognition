package som;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import som.dtw.DTW;
import som.dtw.TimeWarpPoint;
import test.Statistics;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class SOM {

	private static final double START_LEARNING_RATE = 0.35;
	private static final double START_RADIUS = 0.9;

	private static final Random RANDOM = new Random();

	private int numOutput;
	private double[][] weights;
	private int numIteration;
	private int countIteration;
	public String[] outputValueMap;
	private Map<String, Integer>[] outputInputFrequencyMap;

	private Statistics stats;

	public SOM(int numOutput) {
		super();
		this.numOutput = numOutput;
	}

	@SuppressWarnings("unchecked")
	private void initSOM() {
		weights = new double[numOutput][];
		outputValueMap = new String[numOutput];
		outputInputFrequencyMap = new HashMap[numOutput];
		for (int i = 0; i < outputInputFrequencyMap.length; i++) {
			outputInputFrequencyMap[i] = new HashMap<String, Integer>();
		}
		this.stats = new Statistics(numOutput);
	}

	public void train(TrainingSet ts, int numIteration) {
		initSOM();
		this.numIteration = numIteration;
		for (countIteration = 0; countIteration < numIteration; countIteration++) {
			epoch(ts);
			onIteration(countIteration);
		}
		mapOutputs();
	}

	protected void onIteration(int iteration) {
		// do nothing by default
	}

	private void mapOutputs() {
		int max;
		String value;
		for (int i = 0; i < outputInputFrequencyMap.length; i++) {
			Map<String, Integer> outputMap = outputInputFrequencyMap[i];
			max = Integer.MIN_VALUE;
			value = null;
			for (Map.Entry<String, Integer> entry : outputMap.entrySet()) {
				if (entry.getValue() > max) {
					max = entry.getValue();
					value = entry.getKey();
				}
			}
			outputValueMap[i] = value;
		}
	}

	public String findWinnerValue(Input input) {
		return outputValueMap[findWinner(input)];
	}

	public int findWinner(Input input) {
		double[] values = input.getValues();
		int winner = 0;
		double min = Double.MAX_VALUE;
		double currentDist = 0;
		for (int i = 0; i < numOutput; i++) {

			if (weights[i] == null) {
				currentDist = DTW.getDistance(new double[values.length], values, false);
			} else {
				currentDist = DTW.getDistance(weights[i], values, false);
			}

			if (currentDist < min) {
				min = currentDist;
				winner = i;
			}
		}
		return winner;
	}

	private void epoch(TrainingSet ts) {
		int index = RANDOM.nextInt(ts.getInputs().length);
		Input input = ts.getInputs()[index];
		int winner = findWinner(input);
		addToOutputInputMap(winner, input);

		// stats
		stats.add(index, winner);

		adjustWeights(input, winner);
	}

	private void addToOutputInputMap(int output, Input input) {
		Map<String, Integer> outputMap = outputInputFrequencyMap[output];
		String inputName = input.getInputName();
		if (outputMap.get(inputName) == null) {
			outputMap.put(inputName, 1);
		} else {
			outputMap.put(inputName, outputMap.get(inputName) + 1);
		}
	}

	private void adjustWeights(Input input, int winner) {
		double[] values = input.getValues();
		for (int i = 0; i < numOutput; i++) {
			double radius = getNeighbourhoodRadius();
			int distance = Math.abs(winner - i);
			if (distance > Math.round(radius)) {
				continue;
			}

			double factor = getDistanceFactor(distance, radius) * getLearningRate();

			if (weights[i] == null) {
				weights[i] = new double[values.length];
			} else if (values.length > weights[i].length) {
				weights[i] = Arrays.copyOf(weights[i], values.length);
			}

			List<TimeWarpPoint> path = DTW.getWarpPath(weights[i], values);

			for (TimeWarpPoint warpPoint : path) {
				weights[i][warpPoint.getX()] += factor * (values[warpPoint.getY()] - weights[i][warpPoint.getX()]);
			}

		}
	}

	private double getDistanceFactor(double distance, double neighbourhoodRadius) {
		return Math.exp(-distance * distance / (2 * neighbourhoodRadius * neighbourhoodRadius));
	}

	private double getNeighbourhoodRadius() {
		return START_RADIUS * Math.exp(-((double) countIteration) / numIteration);
	}

	private double getLearningRate() {
		return START_LEARNING_RATE * Math.exp(-((double) countIteration) / numIteration);
	}

	public Statistics getStats() {
		return stats;
	}

	public void saveToFile(File file) {
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(Integer.toString(numOutput));
			output.newLine();
			for (int i = 0; i < weights.length; i++) {
				for (int k = 0; k < weights[i].length; k++) {
					output.write(Double.toString(weights[i][k]));
					output.write(";");
				}
				output.newLine();
			}
			for (int i = 0; i < outputValueMap.length; i++) {
				output.write(outputValueMap[i]);
				output.newLine();
			}
			output.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error occured while trying to save the network.");
		}
	}

	public void loadFromFile(File file) {
		try {
			initSOM();
			Scanner scanner = new Scanner(file);
			numOutput = scanner.nextInt();
			scanner.nextLine();
			for (int i = 0; i < numOutput; i++) {
				String line = scanner.nextLine();
				String[] splits = line.split(";");
				double[] array = new double[splits.length];
				for (int j = 0; j < splits.length; j++) {
					array[j] = Double.parseDouble(splits[j]);
				}
				weights[i] = array.clone();
			}
			for (int i = 0; scanner.hasNextLine(); i++) {
				outputValueMap[i] = scanner.nextLine();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Chosen file is not valid.");
		}
	}

}
