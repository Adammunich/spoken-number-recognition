package main;

/**
 * @author niktrk
 * 
 */
public class FFT {

	private static ComplexNumber[] transform(ComplexNumber[] timeDomain, int length, int offset, int skip) {
		ComplexNumber[] frequencyDomain = new ComplexNumber[length];

		if (length == 1) {
			frequencyDomain[0] = timeDomain[offset];
			return frequencyDomain;
		}
		int halfLength = length >> 1;

		ComplexNumber[] evenPart = transform(timeDomain, halfLength, offset, skip * 2);
		ComplexNumber[] oddPart = transform(timeDomain, halfLength, offset + skip, skip * 2);

		double twiddle = -2.0d * Math.PI / length;
		for (int i = 0; i < halfLength; i++) {
			oddPart[i] = oddPart[i].multiply(Math.cos(twiddle * i), Math.sin(twiddle * i));
		}

		for (int i = 0; i < halfLength; i++) {
			frequencyDomain[i] = evenPart[i].add(oddPart[i]);
			frequencyDomain[i + halfLength] = evenPart[i].subtract(oddPart[i]);
		}
		return frequencyDomain;
	}

	public static ComplexNumber[] transform(Frame frame) {
		ComplexNumber[] timeDomain = new ComplexNumber[frame.getWindowedBuffer().length];
		for (int i = 0; i < timeDomain.length; i++) {
			timeDomain[i] = new ComplexNumber(frame.getWindowedBuffer()[i]);
		}
		return transform(timeDomain, timeDomain.length, 0, 1);
	}

	private FFT() {
	}

}
