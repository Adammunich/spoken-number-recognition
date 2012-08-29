package main;

import gui.MainFrame;

import java.util.List;

import javax.swing.JFrame;

import som.Input;
import som.SOM;
import som.TrainingSet;
import dsp.SignalProcessor;
import dsp.util.AudioFileUtil;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class AI {

	private static SOM som;
	private static JFrame mainFrame;

	public static void main(String[] args) {
		initAI();
		initGUI();
	}

	private static void initAI() {
		som = new SOM(3) {

			@Override
			protected void onIteration(int iteration) {

			}

		};
	}

	private static void initGUI() {
		mainFrame = new MainFrame() {

			private static final long serialVersionUID = -541142373678783751L;

			@Override
			protected String recognize(byte[] record) {
				return som.findWinnerValue(new Input(SignalProcessor.process(record), "?"));
			}

			@Override
			protected void train(List<String> filenames, int numIteration) {
				Input[] inputs = new Input[filenames.size()];
				for (int i = 0; i < inputs.length; i++) {
					double[] signal = SignalProcessor.process(AudioFileUtil.readFromFileToByteArray(filenames.get(i)));
					String inputName = AudioFileUtil.getInputValueFromFilename(filenames.get(i));
					inputs[i] = new Input(signal, inputName);
				}
				som.train(new TrainingSet(inputs), numIteration);
			}

		};
		mainFrame.setVisible(true);
	}

}
