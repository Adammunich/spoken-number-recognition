package gui.listeners;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ProgressBarListener implements ChangeListener {

	JProgressBar progressBar;

	public ProgressBarListener(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (progressBar.getValue() >= 100) {
			JOptionPane.showMessageDialog(null, "Your record has been saved.");
			progressBar.setValue(0);
		}
	}
}
