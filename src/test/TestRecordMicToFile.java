package test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import som.Input;
import som.SOM;
import som.TrainingSet;
import dsp.AudioFileUtil;
import dsp.Constants;
import dsp.DCT;
import dsp.DeltaFeatures;
import dsp.EnergyEndPointDetection;
import dsp.FFT;
import dsp.Frame;
import dsp.HammingWindow;
import dsp.MelFilter;
import dsp.PreEmphasisFilter;
import dsp.SubRange;
import dsp.Transformation;
import dsp.WeightedMFCC;

public class TestRecordMicToFile {

	public static void main(String[] args) {

		long start = System.currentTimeMillis();

		// for (int i = 6; i <= 6; i++) {
		// for (int j = 7; j < 8; j++) {
		// System.out.println("write " + i + " " + j);
		// AudioFileUtil.writeFromMicToFile(i + "-" + j + ".wav", 3100);
		// }
		// }

		Input[] inputs = new Input[30];
		int index = 0;
		for (int i = 5; i <= 7; i++) {
			for (int j = 0; j < 10; j++) {
				// System.out.println(i + "-" + j);
				// AudioFileUtil.writeFromMicToFile(i + "-" + j + ".wav", 3100);
				// AudioFileUtil.writeFromMicToFile("test.wav", 3100);

				byte[] signal = AudioFileUtil.readFromFileToByteArray(i + "-" + j + ".wav");
				List<Frame> frames = Frame.getFramesFromByteArray(signal, Constants.FRAME_SAMPLE_LENGTH, 0.5);

				Transformation emphasize = new PreEmphasisFilter();
				Transformation window = new HammingWindow();
				Transformation dft = new FFT();
				for (Frame frame : frames) {
					frame.applyTransformation(emphasize);
					frame.applyTransformation(window);
					frame.applyTransformation(dft);
				}

				EnergyEndPointDetection endPointDetection = new EnergyEndPointDetection(frames);
				List<Frame> removeStartAndEnd = endPointDetection.removeStartAndEnd();
				// System.out.println(frames.size());
				System.out.println(removeStartAndEnd.size());

				Transformation mel = new MelFilter(Constants.MEL_NUMBER_OF_FILTERS);
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
				// System.out.println(index);
				System.out.println(Arrays.toString(vals));
				index++;
			}
			System.out.println();
		}

		TrainingSet ts = new TrainingSet(inputs);

		SOM som = new SOM(Constants.NUMBER_OF_SOM_INPUTS, Constants.NUMBER_OF_SOM_OUTPUTS);
		som.train(ts, 3000);
		som.getStats().print();
		som.getStats().printAggregated(10, 100);
		som.getStats().printCoverage(10, 100);

		System.out.println("Time: " + (System.currentTimeMillis() - start));
	}

	public static void windowedFramesToFile(List<Frame> frames, int index) {
		ByteArrayOutputStream outtrans = new ByteArrayOutputStream();
		int testit = 0;
		for (Frame frame : frames) {
			if (testit % 2 == 0) {
				outtrans.write(frame.getOriginalBuffer(), 0, frame.getOriginalBuffer().length);
			}
			testit++;
		}

		AudioFileUtil.writeFromByteArrayToFile("test" + index + ".wav", outtrans.toByteArray());
	}

}
