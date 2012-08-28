package main;

import gui.MainFrame;

import javax.swing.JFrame;

import som.Input;
import som.SOM;
import dsp.SignalProcessor;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class AI {

	private static SOM som;

	public static void main(String[] args) {
		initAI();
		initGUI();
	}

	private static void initAI() {
		som = new SOM(10);
	}

	private static void initGUI() {
		JFrame frame = new MainFrame() {

			private static final long serialVersionUID = -541142373678783751L;

			@Override
			protected int recognize(byte[] record) {
				return som.findWinnerValue(new Input(SignalProcessor.process(record)));
			}

		};
		frame.setVisible(true);
	}

}
