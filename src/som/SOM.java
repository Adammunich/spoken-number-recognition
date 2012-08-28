package som;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import som.dtw.DTW;
import som.dtw.TimeWarpPoint;
import test.Statistics;

public class SOM {

	private static final double START_LEARNING_RATE = 0.1;
	private static final Random RANDOM = new Random();

	private int numOutput;
	private double startRadius;
	private double[][] weights;
	private int numIteration;
	private int countIteration;

	private Statistics stats;

	public SOM(int numInput, int numOutput) {
		super();
		this.numOutput = numOutput;
		this.startRadius = numOutput / 2;
		this.weights = new double[numOutput][];
		this.stats = new Statistics(numOutput);
	}

	public void train(TrainingSet ts, int numIteration) {
		this.numIteration = numIteration;
		for (countIteration = 0; countIteration < numIteration; countIteration++) {
			epoch(ts);
		}
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
		stats.add(index, winner);
		adjustWeights(input, winner);
	}

	private void adjustWeights(Input input, int winner) {
		double[] values = input.getValues();
		for (int i = 0; i < numOutput; i++) {
			double radius = getNeighbourhoodRadius();
			int distance = Math.abs(winner - i);
			if (distance > Math.round(radius)) {
				continue;
			}
			// System.out.println("distance factor " + winner + " to " + i + " "
			// + getDistanceFactor(distance, radius));

			if (weights[i] == null) {
				weights[i] = new double[values.length];
			}
			double factor = getDistanceFactor(distance, radius) * getLearningRate();

			if (values.length > weights[i].length) {
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
		return startRadius * Math.exp(-((double) countIteration) / numIteration);
	}

	private double getLearningRate() {
		return START_LEARNING_RATE * Math.exp(-((double) countIteration) / numIteration);
	}

	public Statistics getStats() {
		return stats;
	}

}
