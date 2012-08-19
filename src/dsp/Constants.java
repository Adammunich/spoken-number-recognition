package dsp;

import javax.sound.sampled.AudioFormat;

/**
 * @author niktrk
 * 
 */
public final class Constants {

	public static final int SAMPLE_RATE = 8000;
	public static final int MAX_FREQUENCY = SAMPLE_RATE >> 1;
	public static final int MIN_FREQUENCY = 0;
	public static final int FRAME_SAMPLE_LENGTH = 256;
	public static final double FREQUENCY_STEP = ((double) MAX_FREQUENCY) / ((double) (FRAME_SAMPLE_LENGTH >> 1));
	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);

	private Constants() {
	}

}
