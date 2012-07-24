package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;


public class TestRecordMicToFile {

	public static void main(String[] args) throws LineUnavailableException, IOException {

		AudioFormat format = new AudioFormat(8000, 8, 1, true, false);
		TargetDataLine dataLine = AudioSystem.getTargetDataLine(format);
		dataLine.open(format);
		dataLine.start();
		
		int bufferSize = (int) (format.getFrameSize()*format.getFrameRate());
		byte[] buffer = new byte[bufferSize];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < 10000) {
			int count = dataLine.read(buffer, 0, bufferSize);
			if (count > 0) {
				out.write(buffer, 0, count);
			}
		}
		dataLine.drain();
		dataLine.close();
		
		
		byte[] outArray = out.toByteArray();
		AudioInputStream audioInput = new AudioInputStream(new ByteArrayInputStream(outArray), format, outArray.length/format.getFrameSize());
		
		if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioInput)) {
			AudioSystem.write(audioInput, AudioFileFormat.Type.WAVE, new File("test.wav"));
		}
		
	}

}
