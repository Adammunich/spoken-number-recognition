package dsp;

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
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author niktrk
 * 
 */
public class AudioFileUtil {

	private static final AudioFormat AUDIO_FORMAT = new AudioFormat(Constants.SAMPLE_RATE, 8, 1, true, false);

	public static byte[] readFromMicToByteArray(long lengthInMillis) {
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

		return out.toByteArray();
	}

	public static byte[] readFromFileToByteArray(String filename) {
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(AUDIO_FORMAT, AudioSystem.getAudioInputStream(new File(filename)));
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
		AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(byteArray), AUDIO_FORMAT, byteArray.length
				/ AUDIO_FORMAT.getFrameSize());
		writeFromStreamToFile(filename, audioInputStream);
	}

	// public static void windowedFramesToFile(List<Frame> frames, int index) {
	// ByteArrayOutputStream outtrans = new ByteArrayOutputStream();
	// for (int i = 0; i < frames.size(); i += 2) {// because step is 0.5
	// outtrans.write(frames.get(i).getOriginalBuffer(), 0,
	// frames.get(i).getOriginalBuffer().length);
	// }
	// AudioFileUtil.writeFromByteArrayToFile("test" + index + ".wav",
	// outtrans.toByteArray());
	// }

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
