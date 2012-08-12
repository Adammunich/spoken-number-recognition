package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class AproveOpenFolder implements ActionListener {
	public AproveOpenFolder(JFileChooser openFolderDialog, JList<File> fileList) {
		this.openFolderDialog = openFolderDialog;
		this.fileList = fileList;
	}

	private JFileChooser openFolderDialog;
	private JList<File> fileList;

	@Override
	public void actionPerformed(ActionEvent evt) {
		int returnVal = openFolderDialog.showOpenDialog(openFolderDialog.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ArrayList<File> list = new ArrayList<File>();
			getWavFiles(openFolderDialog.getSelectedFile(), list);
			DefaultListModel<File> dlm = (DefaultListModel<File>) fileList.getModel();
			for (int i = 0; i < list.size(); i++) {
				dlm.addElement(list.get(i));
			}
		}
	}

	private void getWavFiles(File current, ArrayList<File> waves) {
		if (current.getName().endsWith("wav")) {
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

class RemoveFileListener implements ActionListener {

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

class SaveToFileListener implements ActionListener {

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

class LoadFromFileListener implements ActionListener {

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
			File file;
			lm.clear();
			while (line != null) {
				line = input.readLine();
				if (line != null) {
					try {
						file = new File(line);
						lm.addElement(file);
					} catch (NullPointerException e1) {
						JOptionPane.showMessageDialog(null, "File " + line + " doesn't exist.");
					}
				}
			}
		} catch (IOException e2) {
		}
	}

}

class RecordListener implements ActionListener, PropertyChangeListener {
	public RecordListener(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	private JProgressBar progressBar;
	Record record;

	@Override
	public void actionPerformed(ActionEvent e) {
		record = new Record();
		record.execute();
		record.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}
}

class ProgressBarListener implements ChangeListener {
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
