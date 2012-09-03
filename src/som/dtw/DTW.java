package som.dtw;

import java.util.LinkedList;
import java.util.List;

import main.Constants;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class DTW {

	private static final int DTW_WINDOW_FACTOR = 10;
	private static final int INITIAL_DTW_MATRIX_SIZE = 800;

	private static double[][] DTW = new double[INITIAL_DTW_MATRIX_SIZE][INITIAL_DTW_MATRIX_SIZE];

	public static List<TimeWarpPoint> getWarpPath(double[] a, double[] b) {
		checkRange(a, b);
		calculateDtwMatrix(a, b);
		int i = a.length;
		int j = b.length;
		double min;
		LinkedList<TimeWarpPoint> path = new LinkedList<TimeWarpPoint>();
		while (i != 0 && j != 0) {
			path.addFirst(new TimeWarpPoint(i - 1, j - 1));
			min = Math.min(DTW[i - 1][j - 1], Math.min(DTW[i - 1][j], DTW[i][j - 1]));
			if (Math.abs(min - DTW[i - 1][j - 1]) < Constants.EPS) {
				i--;
				j--;
			} else if (Math.abs(min - DTW[i - 1][j]) < Constants.EPS) {
				i--;
			} else {
				j--;
			}
		}
		return path;
	}

	public static double getDistance(double[] a, double[] b, boolean normalize) {
		checkRange(a, b);
		calculateDtwMatrix(a, b);
		if (normalize) {
			return DTW[a.length][b.length] / getWarpPathLength(a, b);
		} else {
			return DTW[a.length][b.length];
		}
	}

	private static void checkRange(double[] a, double[] b) {
		if (a.length >= DTW.length || b.length >= DTW.length) {
			DTW = new double[Math.max(a.length, b.length) + 1][Math.max(a.length, b.length) + 1];
		}
	}

	private static void calculateDtwMatrix(double[] a, double[] b) {

		int dwtWindowOffset = Math.max(a.length, b.length) / DTW_WINDOW_FACTOR;
		dwtWindowOffset = Math.max(dwtWindowOffset, Math.abs(a.length - b.length));

		for (int i = 0; i <= a.length; i++) {
			for (int j = 0; j <= b.length; j++) {
				DTW[i][j] = Double.MAX_VALUE;
			}
		}

		DTW[0][0] = 0d;
		int start;
		int end;
		for (int i = 1; i <= a.length; i++) {
			start = Math.max(1, i - dwtWindowOffset);
			end = Math.min(b.length, i + dwtWindowOffset);
			for (int j = start; j <= end; j++) {
				DTW[i][j] = Math.abs(a[i - 1] - b[j - 1]) + Math.min(DTW[i - 1][j - 1], Math.min(DTW[i - 1][j], DTW[i][j - 1]));
			}
		}

	}

	private static int getWarpPathLength(double[] a, double[] b) {
		int i = a.length;
		int j = b.length;
		double min;
		int length = 0;
		while (i != 0 && j != 0) {
			length++;
			min = Math.min(DTW[i - 1][j - 1], Math.min(DTW[i - 1][j], DTW[i][j - 1]));
			if (Math.abs(min - DTW[i - 1][j - 1]) < Constants.EPS) {
				i--;
				j--;
			} else if (Math.abs(min - DTW[i - 1][j]) < Constants.EPS) {
				i--;
			} else {
				j--;
			}
		}
		return length;
	}

	private DTW() {
	}

}
