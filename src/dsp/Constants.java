package dsp;

import javax.sound.sampled.AudioFormat;

/**
 * @author niktrk
 * 
 */
public final class Constants {

	public static final double EPS = 1e-9;

	public static final int SAMPLE_RATE = 8000;
	public static final int FRAME_SAMPLE_LENGTH = 256;
	public static final double FREQUENCY_STEP = ((double) (SAMPLE_RATE >> 1)) / (FRAME_SAMPLE_LENGTH >> 1);
	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);

	public static final int MEL_MIN_FREQUENCY = 200;// 0-350
	public static final int MEL_MAX_FREQUENCY = 3500;// 3500-4000
	public static final int MEL_NUMBER_OF_FILTERS = 31;// 20-40

	public static final int MFCC_LENGTH = 13;

	public static final double MFCC_WIGHT_FACTOR_P = 1d / 3d;
	public static final double MFCC_WIGHT_FACTOR_Q = 1d / 6d;

	public static final double START_LEARNING_RATE = 0.1;
	public static final int NUMBER_OF_SOM_INPUTS = 50 * MFCC_LENGTH;;
	public static final int NUMBER_OF_SOM_OUTPUTS = 3;

	public static final int DWT_WINDOW_OFFSET = NUMBER_OF_SOM_INPUTS / 10;

	private Constants() {
	}

}
