package main;


public class Frame {

	private byte[] buffer;
	private double[] windowedBuffer;
	private ComplexNumber[] transformed;
	private double[] probabilityDensity;

	public Frame(byte[] buffer) {
		super();
		this.buffer = buffer;
	}

	public void applyWindow(Window window) {
		if (windowedBuffer == null) {
			windowedBuffer = new double[buffer.length];
		}
		for (int i = 0; i < buffer.length; i++) {
			windowedBuffer[i] = window.process(buffer[i], i, buffer.length);
		}
	}

	public void transform() {
		transformed = FFT.transform(this);
	}

	public void calculateProbabilityDensity() {
		double totalSpectralEnergy = 0d;
		for (int i = 0; i < transformed.length / 2; i++) {
			totalSpectralEnergy += transformed[i].getSpectralEnergy();
		}
		probabilityDensity = new double[transformed.length / 2];
		for (int i = 0; i < probabilityDensity.length; i++) {
			probabilityDensity[i] = transformed[i].getSpectralEnergy() / totalSpectralEnergy;
		}
	}

	public double calculateSpectralEntropy() {
		double spectralEntropy = 0d;
		for (int i = 0; i < probabilityDensity.length; i++) {
			spectralEntropy += probabilityDensity[i] * Math.log(probabilityDensity[i]);
		}
		return -spectralEntropy;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}

	public ComplexNumber[] getTransformed() {
		return transformed;
	}

	public void setTransformed(ComplexNumber[] transformed) {
		this.transformed = transformed;
	}

	public double[] getWindowedBuffer() {
		return windowedBuffer;
	}

	public void setWindowedBuffer(double[] windowedBuffer) {
		this.windowedBuffer = windowedBuffer;
	}

}
