package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author niktrk
 * 
 */
public class AudioFileUtil {

	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(8000, 8, 1, true, false);

	public static void recordFromMicToFile(String filename, long lengthInMillis) {
		TargetDataLine dataLine = null;
		try {
			dataLine = AudioSystem.getTargetDataLine(AUDIO_FORMAT);
			dataLine.open(AUDIO_FORMAT);
			dataLine.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		int bufferSize = (int) (AUDIO_FORMAT.getFrameSize() * AUDIO_FORMAT.getFrameRate());
		byte[] buffer = new byte[bufferSize];
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < lengthInMillis) {
			int count = dataLine.read(buffer, 0, bufferSize);
			if (count > 0) {
				out.write(buffer, 0, count);
			}
		}
		dataLine.drain();
		dataLine.close();

		writeFromByteArrayToFile(filename, out.toByteArray());
	}

	public static void writeFromByteArrayToFile(String filename, byte[] byteArray) {
		AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(byteArray), AUDIO_FORMAT, byteArray.length
				/ AUDIO_FORMAT.getFrameSize());
		writeFromStreamToFile(filename, audioInputStream);
	}

	public static void writeFromStreamToFile(String filename, AudioInputStream audioInputStream) {
		if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioInputStream)) {
			try {
				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<Frame> getFramesFromFile(String filename, int length, double overlapping) {
		List<Frame> frames = new ArrayList<Frame>();
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(AUDIO_FORMAT, AudioSystem.getAudioInputStream(new File(filename)));
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int offset = length - (int) (length * overlapping);

		byte[] buffer = null;
		try {
			System.out.println(audioStream.available());
			buffer = new byte[audioStream.available()];
			audioStream.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < buffer.length; i += offset) {
			frames.add(new Frame(Arrays.copyOfRange(buffer, i, i + length)));
			System.out.println(i + " - " + (i + length));
		}

		return frames;
	}

	private AudioFileUtil() {
	}

}
