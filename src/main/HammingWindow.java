package main;

/**
 * @author niktrk
 * 
 */
public class HammingWindow implements Window {

	public HammingWindow() {
		super();
	}

	@Override
	public double process(double value, int index, int length) {
		return value * (0.54 - 0.46 * Math.cos(2 * Math.PI * index / (length - 1)));
	}

}
