package dsp.model;

/**
 * @author niktrk
 * 
 */
public class ComplexNumber {

	private double realPart;
	private double imaginaryPart;

	public ComplexNumber(double realPart, double imaginaryPart) {
		super();
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}

	public ComplexNumber(double realPart) {
		this(realPart, 0d);
	}

	public ComplexNumber(ComplexNumber complexNumber) {
		this(complexNumber.realPart, complexNumber.imaginaryPart);
	}

	public ComplexNumber add(ComplexNumber complexNumber) {
		double newRealPart = realPart + complexNumber.getRealPart();
		double newImaginaryPart = imaginaryPart + complexNumber.getImaginaryPart();
		return new ComplexNumber(newRealPart, newImaginaryPart);
	}

	public ComplexNumber multiply(double realPart, double imaginaryPart) {
		double newRealPart = this.realPart * realPart - this.imaginaryPart * imaginaryPart;
		double newImaginaryPart = this.realPart * imaginaryPart + this.imaginaryPart * realPart;
		return new ComplexNumber(newRealPart, newImaginaryPart);
	}

	public ComplexNumber subtract(ComplexNumber complexNumber) {
		double newRealPart = realPart - complexNumber.getRealPart();
		double newImaginaryPart = imaginaryPart - complexNumber.getImaginaryPart();
		return new ComplexNumber(newRealPart, newImaginaryPart);
	}

	public double getMagnitude() {
		return Math.sqrt(realPart * realPart + imaginaryPart * imaginaryPart);
	}

	public double getSpectralEnergy() {
		return realPart * realPart + imaginaryPart * imaginaryPart;
	}

	public double getRealPart() {
		return realPart;
	}

	public void setRealPart(double realPart) {
		this.realPart = realPart;
	}

	public double getImaginaryPart() {
		return imaginaryPart;
	}

	public void setImaginaryPart(double imaginaryPart) {
		this.imaginaryPart = imaginaryPart;
	}

}
