package main;

import gui.MainFrame;

import javax.swing.JFrame;

import dsp.util.AudioFileUtil;

/**
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
			protected void recognize(byte[] record) {
				System.out.println(record.length);
				AudioFileUtil.writeFromByteArrayToFile("testt.wav", record);
			}

		};
		frame.setVisible(true);
	}

}
