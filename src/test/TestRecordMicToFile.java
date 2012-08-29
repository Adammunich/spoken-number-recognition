package test;

import java.util.Arrays;

import som.Input;
import som.SOM;
import som.TrainingSet;
import dsp.SignalProcessor;

public class TestRecordMicToFile {

	public static void main(String[] args) {

		// for (int j = 5; j < 10; j++) {
		// for (int i = 1; i <= 4; i++) {
		// System.out.println("write " + i + " " + j);
		// AudioFileUtil.writeFromMicToFile(i + "-" + j + ".wav", 3100);
		// }
		// }

		Input[] inputs = new Input[30];
		int index = 0;
		for (int i = 3; i <= 5; i++) {
			for (int j = 0; j < 10; j++) {
				inputs[index] = new Input(SignalProcessor.process("audio/nikola/" + i + "-" + j + ".wav"));
				System.out.println(Arrays.toString(inputs[index].getValues()));
				index++;
			}
			System.out.println();
		}

		TrainingSet ts = new TrainingSet(inputs);
		testit(ts);
	}

	private static void testit(TrainingSet ts) {
		for (int i = 0; i < 3; i++) {
			long start = System.currentTimeMillis();
			SOM som = new SOM(3);
			som.train(ts, 3000);
			// som.getStats().print();
			som.getStats().printAggregated(10, 100);
			som.getStats().printCoverage(10, 100);
			System.out.println("Time: " + (System.currentTimeMillis() - start));
		}
	}

}
