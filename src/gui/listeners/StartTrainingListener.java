package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class StartTrainingListener implements ActionListener {
	private JDialog dialog;

	public StartTrainingListener(JDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}

}
