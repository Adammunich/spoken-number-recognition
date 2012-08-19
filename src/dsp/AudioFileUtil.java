package dsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
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

	public static byte[] readFromMicToByteArray(long lengthInMillis) {
		TargetDataLine dataLine = null;
		try {
			dataLine = AudioSystem.getTargetDataLine(Constants.AUDIO_FORMAT);
			dataLine.open(Constants.AUDIO_FORMAT);
			dataLine.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		int bufferSize = (int) (Constants.AUDIO_FORMAT.getFrameSize() * Constants.AUDIO_FORMAT.getFrameRate());
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

		return out.toByteArray();
	}

	public static byte[] readFromFileToByteArray(String filename) {
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(Constants.AUDIO_FORMAT, AudioSystem.getAudioInputStream(new File(filename)));
		} catch (UnsupportedAudioFileException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		byte[] buffer = null;
		try {
			// System.out.println(audioStream.available());
			buffer = new byte[audioStream.available()];
			audioStream.read(buffer, 0, buffer.length);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer;
	}

	public static void writeFromMicToFile(String filename, long lengthInMillis) {
		writeFromByteArrayToFile(filename, readFromMicToByteArray(lengthInMillis));
	}

	public static void writeFromByteArrayToFile(String filename, byte[] byteArray) {
		AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(byteArray), Constants.AUDIO_FORMAT, byteArray.length
				/ Constants.AUDIO_FORMAT.getFrameSize());
		writeFromStreamToFile(filename, audioInputStream);
	}

	private static void writeFromStreamToFile(String filename, AudioInputStream audioInputStream) {
		if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioInputStream)) {
			try {
				AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private AudioFileUtil() {
	}

}
