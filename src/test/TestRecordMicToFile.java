package test;

import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;

import som.Input;
import som.SOM;
import som.TrainingSet;
import dsp.SignalProcessor;
import dsp.util.AudioFileUtil;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class TestRecordMicToFile {

	public static void main(String[] args) {

		// for (int j = 0; j < 1; j++) {
		// for (int i = 0; i < 1; i++) {
		// System.out.println("write " + i);
		// AudioFileUtil.writeFromMicToFile(i + "-" + j + ".wav", 3100);
		// List<Frame> frames =
		// Frame.getFramesFromByteArray(AudioFileUtil.readFromFileToByteArray(i
		// + "-" + j + ".wav"),
		// Constants.FRAME_SAMPLE_LENGTH, 0.5);
		// ThresholdEndPointDetection epd = new EnergyEndPointDetection(frames);
		// System.out.println("----------------------------" +
		// epd.removeStartAndEnd().size());
		// System.out.println();
		// System.out.println();
		// }
		// }

		Input[] inputs = new Input[100];
		int index = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				String filename = "audio/nikola/" + i + "-" + j + "." + AudioFileFormat.Type.WAVE.getExtension();
				inputs[index] = new Input(SignalProcessor.process(filename), AudioFileUtil.getInputValueFromFilename(filename));
				System.out.println(Arrays.toString(inputs[index].getValues()));
				index++;
			}
			System.out.println();
		}

		TrainingSet ts = new TrainingSet(inputs);
		// testit(ts);
	}

	private static void testit(TrainingSet ts) {
		for (int i = 0; i < 1; i++) {
			long start = System.currentTimeMillis();
			SOM som = new SOM(3);
			som.train(ts, 2000);
			// som.getStats().print();
			som.getStats().printAggregated(10, 100);
			som.getStats().printCoverage(10, 100);

			for (int j = 0; j < som.outputValueMap.length; j++) {
				System.out.println("category " + j + ", value: " + som.outputValueMap[j]);
			}

			System.out.println("Time: " + (System.currentTimeMillis() - start));
		}
	}
}
