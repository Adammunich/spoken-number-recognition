package som;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dsp.Constants;

public class SOM {

	private static final Random RANDOM = new Random();

	// private int numInput;
	private int numOutput;
	private double startRadius;
	private double[][] dwt;
	private double[][] weights;
	private int numIteration;
	private int countIteration;

	private Statistics stats;

	public SOM(int numInput, int numOutput) {
		// this.numInput = numInput;
		this.numOutput = numOutput;
		this.startRadius = numOutput / 2;
		this.dwt = new double[numInput + 1][numInput + 1];
		this.weights = new double[numOutput][numInput];
		// for (int i = 0; i < numOutput; i++) {
		// for (int j = 0; j < numInput; j++) {
		// weights[i][j] = RANDOM.nextDouble() / 10000; // small random
		// }
		// }
		this.stats = new Statistics(numOutput);
	}

	public void train(TrainingSet ts, int numIteration) {
		this.numIteration = numIteration;
		this.countIteration = 0;
		boolean ok = true;
		while (countIteration <= numIteration && ok) {
			countIteration++;
			ok = epoch(ts);
		}
	}

	public int findWinner(Input input) {
		double[] values = input.getValues();
		int winner = 0;
		double min = Double.MAX_VALUE;
		double currentDist = 0;
		for (int i = 0; i < numOutput; i++) {
			// System.out.println("dist to " + i + " ");

			// if (weights[i] == null) {
			// currentDist = getDistance(new double[values.length], values);
			// } else {
			currentDist = getDistance(weights[i], values);
			// }

			// System.out.println(currentDist);
			if (currentDist < min) {
				min = currentDist;
				winner = i;
			}
		}
		return winner;
	}

	private boolean epoch(TrainingSet ts) {
		int index = RANDOM.nextInt(ts.getInputs().length);
		Input input = ts.getInputs()[index];
		int winner = findWinner(input);
		stats.add(index, winner);
		adjustWeights(input, winner);
		return true;
	}

	private void adjustWeights(Input input, int winner) {
		double[] values = input.getValues();
		// System.out.println("learning rage " + getLearningRate());
		// System.out.println("neighbourhood rage " + getNeighbourhoodRadius());
		for (int i = 0; i < numOutput; i++) {
			double radius = getNeighbourhoodRadius();
			int distance = Math.abs(winner - i);
			if (distance > Math.round(radius)) {
				continue;
			}
			// System.out.println("distance factor " + winner + " to " + i + " "
			// + getDistanceFactor(distance, radius));
			// for (int j = 0; j < values.length; j++) {
			// weights[i][j] += getDistanceFactor(distance, radius) *
			// getLearningRate() * (values[j] - weights[i][j]);
			// }
			// if (weights[i] == null) {
			// weights[i] = new double[values.length];
			// }
			List<WarpPair> path = getWarpPath(weights[i], values, true);

			// if (path.size() > weights[i].length) {
			// System.out.println("path " + path.size());
			// weights[i] = Arrays.copyOf(weights[i], path.size());
			// }
			//
			// int k = 0;
			// for (WarpPair warpPair : path) {
			// weights[i][k] += getDistanceFactor(distance, radius) *
			// getLearningRate() * (values[warpPair.getJ()] - weights[i][k]);
			// k++;
			// }
			for (WarpPair warpPair : path) {
				weights[i][warpPair.getI()] += getDistanceFactor(distance, radius) * getLearningRate()
						* (values[warpPair.getJ()] - weights[i][warpPair.getI()]);
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
		return Constants.START_LEARNING_RATE * Math.exp(-((double) countIteration) / numIteration);
	}

	// dynamic time warping
	public double getDistance(double[] a, double[] b) {

		int dwtWindowOffset = Math.max(a.length, b.length) / Constants.DWT_WINDOW_FACTOR;
		dwtWindowOffset = Math.max(dwtWindowOffset, Math.abs(a.length - b.length));
		// System.out.println("offset " + dwtWindowOffset);
		// dwtWindowOffset = 5;

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

		// for (int i = 0; i < dwt.length; i++) {
		// for (int j = 0; j < dwt.length; j++) {
		// System.out.print(dwt[i][j] + "   ");
		// }
		// System.out.println();
		// }

		return dwt[a.length][b.length] / getWarpPath(a, b, false).size();
	}

	// TODO?
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

	// public static void main(String[] arg) {
	// SOM som = new SOM(5, 5);
	// System.out.println(som.getWarpPath(new double[] { 2, 2, 3, 2, 2 }, new
	// double[] { 2, 1, 2 }));
	// }

}
