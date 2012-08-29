package gui.listeners;

import gui.Record;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JProgressBar;

public abstract class RecordListener implements ActionListener, PropertyChangeListener {

	private JProgressBar progressBar;

	public RecordListener(JProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		progressBar.setValue(0);
		Record record = new Record() {

			@Override
			protected void done() {
				try {
					onRecordingDone(get());
				} catch (InterruptedException | ExecutionException e) {
					throw new IllegalStateException("Can't get recording: " + e.getMessage());
				}
			}

		};
		record.execute();
		record.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	protected abstract void onRecordingDone(byte[] recording);

}