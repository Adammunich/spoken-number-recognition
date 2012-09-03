package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
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
