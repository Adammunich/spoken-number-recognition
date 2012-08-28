package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class RemoveFileListener implements ActionListener {

	private JList<File> fileList;

	public RemoveFileListener(JList<File> fileList) {
		this.fileList = fileList;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = fileList.getSelectedIndex();
		if (index > -1) {
			DefaultListModel<File> dlm = (DefaultListModel<File>) fileList.getModel();
			dlm.remove(index);
		}
	}
}