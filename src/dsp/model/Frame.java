package dsp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dsp.transformation.Transformation;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class Frame {

	private byte[] originalBuffer;
	private double[] buffer;

	public Frame(byte[] originalBuffer) {
		super();
		this.originalBuffer = originalBuffer;
		this.buffer = new double[originalBuffer.length];
		for (int i = 0; i < originalBuffer.length; i++) {
			this.buffer[i] = originalBuffer[i];
		}
	}

	public void applyTransformation(Transformation transformation) {
		buffer = transformation.transform(buffer);
	}

	public byte[] getOriginalBuffer() {
		return originalBuffer;
	}

	public void setOriginalBuffer(byte[] originalBuffer) {
		this.originalBuffer = originalBuffer;
	}

	public double[] getBuffer() {
		return buffer;
	}

	public void setBuffer(double[] buffer) {
		this.buffer = buffer;
	}

	public static List<Frame> getFramesFromByteArray(byte[] bytes, int length, double overlapping) {
		List<Frame> frames = new ArrayList<Frame>();
		int offset = length - (int) (length * overlapping);
		for (int i = 0; i < bytes.length; i += offset) {
			frames.add(new Frame(Arrays.copyOfRange(bytes, i, i + length)));
		}
		return frames;
	}

}
