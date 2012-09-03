package main;

import gui.MainFrame;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
	private static MainFrame mainFrame;

	public static void main(String[] args) {
		initAI();
		initGUI();
	}

	private static void initAI() {
		som = new SOM(16) {

			@Override
			protected void onIteration(int iteration) {
				mainFrame.setTriainingProgress(iteration);
			}

		};
	}

	private static void initGUI() {
		mainFrame = new MainFrame() {

			private static final long serialVersionUID = -541142373678783751L;

			@Override
			protected String recognize(byte[] record) {
				double[] signal = SignalProcessor.process(record);
				if (signal != null) {
					return som.findWinnerValue(new Input(signal, "?"));
				}
				return null;
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
				// som.getStats().printAggregated(10, 100);
				System.out.println(Arrays.toString(som.outputValueMap));
			}

			@Override
			protected void saveSomToFile(File file) {
				som.saveToFile(file);
			}

			@Override
			protected void loadSomFromFile(File file) {
				som.loadFromFile(file);
			}

		};
		mainFrame.setVisible(true);
	}

}
