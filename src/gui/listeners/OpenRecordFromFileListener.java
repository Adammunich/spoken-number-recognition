package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import dsp.util.AudioFileUtil;

public abstract class OpenRecordFromFileListener implements ActionListener {
	private JFileChooser dialog;

	public OpenRecordFromFileListener(JFileChooser dialog) {
		this.dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.showOpenDialog(dialog.getParent());
		if (dialog.getSelectedFile() != null) {
			byte[] record = AudioFileUtil.readFromFileToByteArray(dialog.getSelectedFile().getAbsolutePath());
			JOptionPane.showMessageDialog(null, "Your recored has been loaded.");
			onRecordLoaded(record);
		}
	}

	protected abstract void onRecordLoaded(byte[] record);

}
