package test;

import java.util.Arrays;
import java.util.List;

import main.Constants;
import som.Input;
import som.SOM;
import som.TrainingSet;
import dsp.endpointdetection.EnergyEndPointDetection;
import dsp.endpointdetection.ThresholdEndPointDetection;
import dsp.model.Frame;
import dsp.transformation.DCT;
import dsp.transformation.DeltaFeatures;
import dsp.transformation.FFT;
import dsp.transformation.HammingWindow;
import dsp.transformation.MelFilter;
import dsp.transformation.PreEmphasisFilter;
import dsp.transformation.SubRange;
import dsp.transformation.Transformation;
import dsp.transformation.WeightedMFCC;
import dsp.util.AudioFileUtil;

public class TestRecordMicToFile {

	private static final int MEL_NUMBER_OF_FILTERS = 31;// 20-40

	public static void main(String[] args) {

		// for (int j = 5; j < 10; j++) {
		// for (int i = 1; i <= 4; i++) {
		// System.out.println("write " + i + " " + j);
		// AudioFileUtil.writeFromMicToFile(i + "-" + j + ".wav", 3100);
		// }
		// }

		Input[] inputs = new Input[60];
		int index = 0;
		for (int i = 1; i <= 6; i++) {
			for (int j = 0; j < 10; j++) {

				byte[] signal = AudioFileUtil.readFromFileToByteArray("audio/nikola/" + i + "-" + j + ".wav");
				List<Frame> frames = Frame.getFramesFromByteArray(signal, Constants.FRAME_SAMPLE_LENGTH, 0.5);

				Transformation emphasize = new PreEmphasisFilter();
				Transformation window = new HammingWindow();
				Transformation dft = new FFT();
				for (Frame frame : frames) {
					frame.applyTransformation(emphasize);
					frame.applyTransformation(window);
					frame.applyTransformation(dft);
				}

				ThresholdEndPointDetection endPointDetection = new EnergyEndPointDetection(frames);
				List<Frame> removeStartAndEnd = endPointDetection.removeStartAndEnd();
				System.out.println(removeStartAndEnd.size());

				Transformation mel = new MelFilter(MEL_NUMBER_OF_FILTERS);
				Transformation dct = new DCT();
				Transformation sub = new SubRange(1, 1 + Constants.MFCC_LENGTH);
				Transformation delta = new DeltaFeatures(2);
				Transformation weight = new WeightedMFCC();
				double[] vals = new double[removeStartAndEnd.size() * Constants.MFCC_LENGTH];
				int k = 0;
				for (Frame frame : removeStartAndEnd) {
					frame.applyTransformation(mel);
					frame.applyTransformation(dct);
					frame.applyTransformation(sub);
					frame.applyTransformation(delta);
					frame.applyTransformation(weight);
					System.arraycopy(frame.getBuffer(), 0, vals, k * Constants.MFCC_LENGTH, Constants.MFCC_LENGTH);
					k++;
				}

				inputs[index] = new Input(vals);
				System.out.println(Arrays.toString(vals));
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
			SOM som = new SOM(Constants.NUMBER_OF_SOM_INPUTS, 6);
			som.train(ts, 12000);
			// som.getStats().print();
			som.getStats().printAggregated(10, 100);
			som.getStats().printCoverage(10, 100);
			System.out.println("Time: " + (System.currentTimeMillis() - start));
		}
	}

}
