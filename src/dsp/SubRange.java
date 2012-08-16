package dsp;

import java.util.Arrays;

/**
 * @author niktrk
 * 
 */
public class SubRange implements Transformation {

	private final int start;
	private final int end;

	public SubRange(int start, int end) {
		super();
		this.start = start;
		this.end = end;
	}

	@Override
	public double[] transform(double[] input) {
		return Arrays.copyOfRange(input, start, end);
	}

}
