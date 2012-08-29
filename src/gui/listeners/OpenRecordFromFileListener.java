package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dsp.util.AudioFileUtil;

public class OpenRecordFromFileListener implements ActionListener {
	private JFileChooser dialog;
	private byte[] record;

	public OpenRecordFromFileListener(byte[] record, JFileChooser dialog) {
		this.dialog = dialog;
		this.record = record;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.showOpenDialog(dialog.getParent());
		if (dialog.getSelectedFile() != null) {
			record = AudioFileUtil.readFromFileToByteArray(dialog.getSelectedFile().getAbsolutePath());
			JOptionPane.showMessageDialog(null, "Your recored has been loaded.");

		}
	}

	public byte[] getRecord() {
		return record;
	}

	public void setRecord(byte[] record) {
		this.record = record;
	}
}
