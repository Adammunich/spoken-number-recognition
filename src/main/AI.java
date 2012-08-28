package main;

import gui.MainFrame;

import java.util.Random;

import javax.swing.JFrame;

import dsp.util.AudioFileUtil;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class AI {

	public static void main(String[] args) {
		initGUI();
	}

	private static void initGUI() {
		JFrame frame = new MainFrame() {

			private static final long serialVersionUID = -541142373678783751L;

			@Override
			protected int recognize(byte[] record) {
				System.out.println(record.length);
				AudioFileUtil.writeFromByteArrayToFile("testt.wav", record);
				return new Random().nextInt(10);
			}

		};
		frame.setVisible(true);
	}

}
