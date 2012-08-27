package dsp;

/**
 * @author niktrk
 * 
 */
public final class Constants {

	public static final double EPS = 1e-9;

	public static final int SAMPLE_RATE = 8000;
	public static final int FRAME_SAMPLE_LENGTH = 256;
	public static final double FREQUENCY_STEP = ((double) (SAMPLE_RATE >> 1)) / (FRAME_SAMPLE_LENGTH >> 1);

	public static final int MFCC_LENGTH = 13;

	public static final int NUMBER_OF_SOM_INPUTS = 55 * MFCC_LENGTH;
	public static final int NUMBER_OF_SOM_OUTPUTS = 3;

	private Constants() {
	}

}
