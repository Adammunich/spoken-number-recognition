package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import som.Input;
import som.SOM;
import som.TrainingSet;
import dsp.AudioFileUtil;
import dsp.CMN;
import dsp.Constants;
import dsp.DCT;
import dsp.EndPointDetection;
import dsp.FFT;
import dsp.Frame;
import dsp.HammingWindow;
import dsp.MelFilter;
import dsp.PreEmphasisFilter;
import dsp.SubRange;
import dsp.Transformation;

public class TestRecordMicToFile {

	public static void main(String[] args) {

		Input[] inputs = new Input[15];
		int index = 0;
		for (int i = 5; i <= 7; i++) {
			for (int j = 0; j < 5; j++) {
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

				EndPointDetection endPointDetection = new EndPointDetection(frames);
				List<Frame> removeStartAndEnd = endPointDetection.removeStartAndEnd();
				// System.out.println(frames.size());
				// System.out.println(removeStartAndEnd.size());

				Transformation mel = new MelFilter(23);
				Transformation dct = new DCT();
				Transformation sub = new SubRange(1, 14);
				for (Frame frame : removeStartAndEnd) {
					frame.applyTransformation(mel);
					frame.applyTransformation(dct);
					frame.applyTransformation(sub);
				}

				double[] vals = new double[50 * 13];
				int k = 0;
				Transformation cmn = new CMN(removeStartAndEnd);
				for (Frame frame : removeStartAndEnd) {
					frame.applyTransformation(cmn);
					for (int m = 0; m < frame.getBuffer().length; m++) {
						vals[k * 13 + m] = frame.getBuffer()[m];
					}
					k++;
				}

				inputs[index] = new Input(vals);
				System.out.println(Arrays.toString(vals));
				index++;
			}
		}

		TrainingSet ts = new TrainingSet(inputs);

		SOM som = new SOM(50 * 13, 3);
		som.train(ts, 5000);

		// ts.printMe();
		// som.printMe();

		List<Integer> in = ts.getList();
		List<Integer> out = som.getList();
		Map<Integer, List<Integer>> res = new HashMap<Integer, List<Integer>>();
		res.put(0, new ArrayList<Integer>());
		res.put(1, new ArrayList<Integer>());
		res.put(2, new ArrayList<Integer>());
		for (int i = 0; i < out.size(); i++) {
			res.get(out.get(i)).add(in.get(i));
		}
		for (List<Integer> list : res.values()) {
			System.out.println(list);
		}

		// ByteArrayOutputStream outtrans = new ByteArrayOutputStream();
		// int testit = 0;
		// for (Frame frame : removeStartAndEnd) {
		// if (testit % 2 == 0) {
		// outtrans.write(frame.getOriginalBuffer(), 0,
		// frame.getOriginalBuffer().length);
		// }
		// testit++;
		// }
		//
		// AudioFileUtil.writeFromByteArrayToFile("test1.wav",
		// outtrans.toByteArray());
	}
}
