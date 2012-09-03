package gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public class AproveOpenFolder implements ActionListener {

	private JFileChooser openFolderDialog;
	private JList<File> fileList;

	public AproveOpenFolder(JFileChooser openFolderDialog, JList<File> fileList) {
		this.openFolderDialog = openFolderDialog;
		this.fileList = fileList;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		int returnVal = openFolderDialog.showOpenDialog(openFolderDialog.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			List<File> list = new ArrayList<File>();
			getWavFiles(openFolderDialog.getSelectedFile(), list);
			DefaultListModel<File> dlm = (DefaultListModel<File>) fileList.getModel();
			for (int i = 0; i < list.size(); i++) {
				dlm.addElement(list.get(i));
			}
		}
	}

	private void getWavFiles(File current, List<File> waves) {
		if (current.getName().endsWith(AudioFileFormat.Type.WAVE.getExtension())) {
			waves.add(current);
		}
		if (current.isDirectory()) {
			File[] list = current.listFiles();
			for (int i = 0; i < list.length; i++) {
				getWavFiles(list[i], waves);
			}
		}

	}

}