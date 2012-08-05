package test;

import java.io.ByteArrayOutputStream;
import java.util.List;

import main.AudioFileUtil;
import main.EndPointDetection;
import main.Frame;
import main.HammingWindow;
import main.Window;

public class TestRecordMicToFile {

	public static void main(String[] args) {

		AudioFileUtil.recordFromMicToFile("test.wav", 3100);

		List<Frame> frames = AudioFileUtil.getFramesFromFile("test.wav", 256, 0.5);
		Window window = new HammingWindow();
		for (Frame frame : frames) {
			frame.applyWindow(window);
			frame.transform();
			frame.calculateProbabilityDensity();
		}
		EndPointDetection endPointDetection = new EndPointDetection(frames);
		List<Frame> removeStartAndEnd = endPointDetection.removeStartAndEnd();
		System.out.println(frames.size());
		System.out.println(removeStartAndEnd.size());

		ByteArrayOutputStream outtrans = new ByteArrayOutputStream();
		int testit = 0;
		for (Frame frame : removeStartAndEnd) {
			if (testit % 2 == 0) {
				outtrans.write(frame.getBuffer(), 0, frame.getBuffer().length);
			}
			testit++;
		}

		AudioFileUtil.writeFromByteArrayToFile("test1.wav", outtrans.toByteArray());
	}

}
