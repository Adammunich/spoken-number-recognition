package gui;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.SwingWorker;

import main.Constants;

/**
 * @author igorletso
 * @author niktrk
 * 
 */
public class Record extends SwingWorker<byte[], Byte> {

	private static final int RECORD_LENGTH = 3000;
	private static final int RECORD_STEP = RECORD_LENGTH / 100;

	@Override
	protected byte[] doInBackground() throws Exception {
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
		int progress = 0;
		setProgress(0);
		while (progress < RECORD_LENGTH) {
			int count = dataLine.read(buffer, 0, bufferSize);
			if (count > 0) {
				out.write(buffer, 0, count);
			}
			progress = (int) (System.currentTimeMillis() - start);
			setProgress(Math.min(RECORD_LENGTH, progress) / RECORD_STEP);
		}
		dataLine.drain();
		dataLine.close();
		setProgress(100);
		return out.toByteArray();
	}

}
