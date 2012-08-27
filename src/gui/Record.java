package gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingWorker;

/**
 * @author igorletso
 * 
 */
public class Record extends SwingWorker<Void, Void> {

	@Override
	protected Void doInBackground() throws Exception {
		AudioFormat format = new AudioFormat(8000, 8, 1, true, false);
		TargetDataLine dataLine = AudioSystem.getTargetDataLine(format);
		dataLine.open(format);
		dataLine.start();

		int bufferSize = (int) (format.getFrameSize() * format.getFrameRate());
		byte[] buffer = new byte[bufferSize];
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		long start = System.currentTimeMillis();
		int progress = (int) (System.currentTimeMillis() - start);
		setProgress(0);
		while (progress < 5000) {
			int count = dataLine.read(buffer, 0, bufferSize);
			if (count > 0) {
				out.write(buffer, 0, count);
			}
			progress = (int) (System.currentTimeMillis() - start);
			setProgress(progress / 51);
		}
		dataLine.drain();
		dataLine.close();

		byte[] outArray = out.toByteArray();

		AudioInputStream audioInput = new AudioInputStream(new ByteArrayInputStream(outArray), format, outArray.length / format.getFrameSize());

		if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, audioInput)) {
			AudioSystem.write(audioInput, AudioFileFormat.Type.WAVE, new File("test.wav"));
		}
		audioInput = AudioSystem.getAudioInputStream(new File("test.wav"));
		setProgress(100);
		return null;
	}

	@Override
	public void done() {
		setProgress(100);

	}

}
