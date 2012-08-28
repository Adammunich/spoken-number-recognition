package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;

public class SaveToFileListener implements ActionListener {

	private JList<File> fileList;
	private JFileChooser dialog;

	public SaveToFileListener(JList<File> fileList, JFileChooser dialog) {
		this.fileList = fileList;
		this.dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			DefaultListModel<File> lm = (DefaultListModel<File>) fileList.getModel();
			dialog.showSaveDialog(dialog.getParent());
			BufferedWriter output = new BufferedWriter(new FileWriter(dialog.getSelectedFile()));
			for (int i = 0; i < lm.size(); i++) {
				output.write(lm.getElementAt(i).toString());
				output.newLine();
			}
			output.close();
		} catch (IOException e) {
		}

	}

}