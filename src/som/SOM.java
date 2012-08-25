package som;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dsp.Constants;

public class SOM {

	private static final Random RANDOM = new Random();

	private int numOutput;
	private double startRadius;
	private double[][] dwt;
	private double[][] weights;
	private int numIteration;
	private int countIteration;

	private Statistics stats;

	public SOM(int numInput, int numOutput) {
		super();
		this.numOutput = numOutput;
		this.startRadius = numOutput / 2;
		this.dwt = new double[numInput + 1][numInput + 1];
		this.weights = new double[numOutput][numInput];
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

			// if (weights[i] == null) {
			// currentDist = getDistance(new double[values.length], values);
			// } else {
			currentDist = getDistance(weights[i], values);
			// }

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

			// if (weights[i] == null) {
			// weights[i] = new double[values.length];
			// }
			double factor = getDistanceFactor(distance, radius) * getLearningRate();

			// if (values.length > weights[i].length) {
			// weights[i] = Arrays.copyOf(weights[i], (int)
			// Math.round(weights[i].length + factor * (values.length -
			// weights[i].length)));
			// }

			List<WarpPair> path = getWarpPath(weights[i], values, true);

			for (WarpPair warpPair : path) {
				weights[i][warpPair.getI()] += factor * (values[warpPair.getJ()] - weights[i][warpPair.getI()]);
			}

		}
	}

	// public double[] adjuster(double[] a, double[] b, double factor) {
	// double[] newWights = new double[(int) Math.round(a.length + factor *
	// (b.length - a.length))];
	//
	// List<WarpPair> path = getWarpPath(a, b, true);
	//
	// for (int j = 0; j < newWights.length; j++) {
	// for (int k = 0; k < path.size(); k++) {
	// WarpPair warpPair = path.get(k);
	// double pairIndex = warpPair.getI() + factor * (warpPair.getJ() -
	// warpPair.getI());
	//
	// if (Math.abs(pairIndex - j) < Constants.EPS) {
	// newWights[j] = a[warpPair.getI()] + factor * (b[warpPair.getJ()] -
	// a[warpPair.getI()]);
	// break;
	// } else if (k < path.size() - 1) {
	//
	// WarpPair warpPairNext = path.get(k + 1);
	// double pairIndexNext = warpPairNext.getI() + factor *
	// (warpPairNext.getJ() - warpPairNext.getI());
	//
	// if (pairIndex < j && j < pairIndexNext) {
	// double m1 = a[warpPair.getI()] + factor * (b[warpPair.getJ()] -
	// a[warpPair.getI()]);
	// double m2 = a[warpPairNext.getI()] + factor * (b[warpPairNext.getJ()] -
	// a[warpPairNext.getI()]);
	// double q1 = pairIndexNext - j;
	// double q2 = j - pairIndex;
	//
	// newWights[j] = (q1 * m1 + q2 * m2) / (q1 + q2);
	// break;
	// }
	//
	// }
	// }
	// }
	//
	// return newWights;
	// }

	private double getDistanceFactor(double distance, double neighbourhoodRadius) {
		return Math.exp(-distance * distance / (2 * neighbourhoodRadius * neighbourhoodRadius));
	}

	private double getNeighbourhoodRadius() {
		return startRadius * Math.exp(-((double) countIteration) / numIteration);
	}

	private double getLearningRate() {
		return Constants.START_LEARNING_RATE * Math.exp(-((double) countIteration) / numIteration);
	}

	// dynamic time warping
	public double getDistance(double[] a, double[] b) {

		int dwtWindowOffset = Math.max(a.length, b.length) / Constants.DTW_WINDOW_FACTOR;
		dwtWindowOffset = Math.max(dwtWindowOffset, Math.abs(a.length - b.length));

		for (int i = 0; i <= a.length; i++) {
			for (int j = 0; j <= b.length; j++) {
				dwt[i][j] = Double.MAX_VALUE;
			}
		}
		dwt[0][0] = 0d;
		int start;
		int end;
		for (int i = 1; i <= a.length; i++) {
			start = Math.max(1, i - dwtWindowOffset);
			end = Math.min(b.length, i + dwtWindowOffset);
			for (int j = start; j <= end; j++) {
				dwt[i][j] = Math.abs(a[i - 1] - b[j - 1]) + Math.min(dwt[i - 1][j - 1], Math.min(dwt[i - 1][j], dwt[i][j - 1]));
			}
		}

		return dwt[a.length][b.length] / getWarpPath(a, b, false).size();
	}

	public List<WarpPair> getWarpPath(double[] a, double[] b, boolean calcDist) {
		if (calcDist) {
			getDistance(a, b);
		}
		int i = a.length;
		int j = b.length;
		double min;
		List<WarpPair> path = new ArrayList<WarpPair>();
		while (i != 0 && j != 0) {
			path.add(new WarpPair(i - 1, j - 1));
			min = Math.min(dwt[i - 1][j - 1], Math.min(dwt[i - 1][j], dwt[i][j - 1]));
			if (Math.abs(min - dwt[i - 1][j - 1]) < Constants.EPS) {
				i--;
				j--;
			} else if (Math.abs(min - dwt[i - 1][j]) < Constants.EPS) {
				i--;
			} else {
				j--;
			}
		}
		Collections.reverse(path);
		return path;
	}

	public Statistics getStats() {
		return stats;
	}

	private static final class WarpPair {
		private final int i, j;

		public WarpPair(int i, int j) {
			super();
			this.i = i;
			this.j = j;
		}

		public int getI() {
			return i;
		}

		public int getJ() {
			return j;
		}

		@Override
		public String toString() {
			return "[" + i + "," + j + "]";
		}

	}

}
