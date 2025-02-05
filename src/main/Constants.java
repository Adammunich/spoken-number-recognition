package main;

import javax.sound.sampled.AudioFormat;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public final class Constants {

	public static final double EPS = 1e-9;

	public static final int SAMPLE_RATE = 8000;
	public static final int FRAME_SAMPLE_LENGTH = 256;
	public static final double FREQUENCY_STEP = ((double) (SAMPLE_RATE >> 1)) / (FRAME_SAMPLE_LENGTH >> 1);

	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);

	private Constants() {
	}

}
