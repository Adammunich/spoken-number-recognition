package test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author niktrk
 * 
 */
public class Statistics {

	private Map<Integer, LinkedList<Integer>> stats;
	private final int numberOfOutputs;

	public Statistics(int numberOfOutputs) {
		super();
		this.numberOfOutputs = numberOfOutputs;
		stats = new HashMap<Integer, LinkedList<Integer>>();
		for (int i = 0; i < numberOfOutputs; i++) {
			stats.put(i, new LinkedList<Integer>());
		}
	}

	public void add(int input, int output) {
		stats.get(output).addFirst(input);
	}

	public void print() {
		for (List<Integer> list : stats.values()) {
			System.out.println(list);
		}
	}

	public void printAggregated(int inputClassLength, int aggregationLength) {
		for (List<Integer> list : stats.values()) {
			int[] agg = new int[numberOfOutputs];
			int i = 0;
			for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext() && i < aggregationLength; i++) {
				agg[iterator.next() / inputClassLength]++;
			}
			System.out.println(Arrays.toString(agg));
		}
	}

	public void printCoverage(int inputClassLength, int coverageLength) {
		for (List<Integer> list : stats.values()) {
			int[] coverage = new int[numberOfOutputs * inputClassLength];
			int i = 0;
			for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext() && i < coverageLength; i++) {
				coverage[iterator.next()]++;
			}
			for (int j = 0; j < numberOfOutputs; j++) {
				System.out.print(Arrays.toString(Arrays.copyOfRange(coverage, j * inputClassLength, (j + 1) * inputClassLength)));
			}
			System.out.println();
		}
	}

}
