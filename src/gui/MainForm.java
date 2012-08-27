package gui;

import java.awt.Container;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

/**
 * @author igorletso
 * 
 */
public class MainForm extends JApplet {

	private static final long serialVersionUID = -3838320390904637165L;

	void setLAF() {
		String laf = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(laf);
		} catch (UnsupportedLookAndFeelException e) {
			JOptionPane.showOptionDialog(null, e.getMessage(), "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, "OK");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	@Override
	public void init() {
		setLAF();
		JPanel p = new JPanel(new MigLayout());

		JPanel up = new JPanel(new MigLayout());
		JPanel down = new JPanel(new MigLayout());
		DefaultListModel<File> model = new DefaultListModel<File>();
		JList<File> fileList = new JList<File>(model);
		JButton openFolder = new JButton("Find training data");
		JButton removeFile = new JButton("Remove file");
		JButton saveData = new JButton("Save training data to file");
		JButton loadData = new JButton("Load training data from file");
		JButton startTraining = new JButton("Start training");

		Font labelFont = new Font("Arial", Font.PLAIN, 15);
		JLabel recDig = new JLabel("Recognized digit: ");
		JLabel digit = new JLabel("5");
		recDig.setFont(labelFont);
		digit.setFont(labelFont);

		JButton record = new JButton("Record");
		JButton openFile = new JButton("Open from file");
		JButton recognize = new JButton("Recognize");
		JProgressBar progressBar = new JProgressBar(0, 101);
		progressBar.setValue(0);

		JFileChooser openFolderDialog = new JFileChooser();
		openFolderDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JFileChooser saveFileDialog = new JFileChooser();
		JFileChooser loadFileDialog = new JFileChooser();

		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		menu.add(fileMenu);
		menu.add(helpMenu);
		setJMenuBar(menu);

		Container cp = getContentPane();
		cp.add(p);

		up.setBorder(BorderFactory.createTitledBorder("Training"));
		up.add(fileList, "span 1 5, height 200:200:200, width 300:300:300");
		up.add(openFolder, "wrap");
		up.add(removeFile, "wrap");
		up.add(saveData, "wrap");
		up.add(loadData, "wrap 10px");
		up.add(startTraining);

		down.setBorder(BorderFactory.createTitledBorder("Recognition"));
		down.add(record, "align center");
		down.add(openFile, "split 2");
		down.add(recognize, "wrap");
		down.add(progressBar, "wrap 15px");
		down.add(recDig, "split 2");
		down.add(digit);

		p.add(up, "span, height 250:250:250, width 750:750:750");
		p.add(down, "span, height 130:130:130, width 750:750:750");

		AproveOpenFolder aof = new AproveOpenFolder(openFolderDialog, fileList);
		openFolder.addActionListener(aof);

		RemoveFileListener rfl = new RemoveFileListener(fileList);
		removeFile.addActionListener(rfl);

		SaveToFileListener sfl = new SaveToFileListener(fileList, saveFileDialog);
		saveData.addActionListener(sfl);

		LoadFromFileListener lffl = new LoadFromFileListener(fileList, loadFileDialog);
		loadData.addActionListener(lffl);

		RecordListener rl = new RecordListener(progressBar);
		record.addActionListener(rl);

		ProgressBarListener pbl = new ProgressBarListener(progressBar);
		progressBar.addChangeListener(pbl);
	}

}
