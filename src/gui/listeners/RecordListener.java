package gui.listeners;

import gui.Record;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JProgressBar;

public class RecordListener implements ActionListener, PropertyChangeListener {

	private JProgressBar progressBar;
	private Record record;

	public RecordListener(JProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		record = new Record();
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

	public byte[] getRecording() {
		try {
			if (record == null) {
				return null;
			} else {
				return record.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			throw new IllegalStateException("Can't get recording: " + e.getMessage());
		}
	}

}