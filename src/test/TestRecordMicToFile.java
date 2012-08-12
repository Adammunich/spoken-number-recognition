package test;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import dsp.AudioFileUtil;
import dsp.DCT;
import dsp.EndPointDetection;
import dsp.FFT;
import dsp.Frame;
import dsp.HammingWindow;
import dsp.MelFilter;
import dsp.PreEmphasisFilter;
import dsp.Transformation;


public class TestRecordMicToFile {

	public static void main(String[] args) {

		// AudioFileUtil.writeFromMicToFile("test.wav", 3100);
		byte[] signal = AudioFileUtil.readFromFileToByteArray("test.wav");
		List<Frame> frames = Frame.getFramesFromByteArray(signal, 256, 0.5);

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
		System.out.println(frames.size());
		System.out.println(removeStartAndEnd.size());

		Transformation mel = new MelFilter(30);
		Transformation dct = new DCT();
		for (Frame frame : removeStartAndEnd) {
			frame.applyTransformation(mel);
			System.out.println(Arrays.toString(frame.getBuffer()));
			frame.applyTransformation(dct);
		}

		ByteArrayOutputStream outtrans = new ByteArrayOutputStream();
		int testit = 0;
		for (Frame frame : removeStartAndEnd) {
			if (testit % 2 == 0) {
				outtrans.write(frame.getOriginalBuffer(), 0, frame.getOriginalBuffer().length);
			}
			testit++;
		}

		AudioFileUtil.writeFromByteArrayToFile("test1.wav", outtrans.toByteArray());
	}
}
