package main;

import gui.MainForm;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 * @author niktrk
 * 
 */
public class AI {

	public static void main(String[] args) {
		// create frame
		JFrame frame = new JFrame("Spoken digits recognition");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		// create applet
		JApplet applet = new MainForm();
		applet.init();
		// initialize frame
		frame.getContentPane().add(applet);
		frame.setSize(780, 500);
		// set frame position
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
		frame.setVisible(true);
	}

}
