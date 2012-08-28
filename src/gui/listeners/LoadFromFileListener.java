package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class LoadFromFileListener implements ActionListener {

	private JList<File> fileList;
	private JFileChooser dialog;

	public LoadFromFileListener(JList<File> fileList, JFileChooser dialog) {
		this.fileList = fileList;
		this.dialog = dialog;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			DefaultListModel<File> lm = (DefaultListModel<File>) fileList.getModel();
			dialog.showOpenDialog(dialog.getParent());
			BufferedReader input = new BufferedReader(new FileReader(dialog.getSelectedFile()));
			String line = "";
			lm.clear();
			while (line != null) {
				line = input.readLine();
				if (line != null) {
					try {
						lm.addElement(new File(line));
					} catch (NullPointerException e1) {
						JOptionPane.showMessageDialog(null, "File " + line + " doesn't exist.");
					}
				}
			}
		} catch (IOException e2) {
		}
	}

}