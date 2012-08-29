package gui;

import gui.listeners.AproveOpenFolder;
import gui.listeners.LoadFromFileListener;
import gui.listeners.ProgressBarListener;
import gui.listeners.RecordListener;
import gui.listeners.RemoveFileListener;
import gui.listeners.SaveToFileListener;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public abstract class MainFrame extends JFrame {

	private static final long serialVersionUID = -3838320390904637165L;

	public MainFrame() {
		super("Spoken digits recognition");
		initFrame();
		setPosition();
		setLAF();
		initComponents();
	}

	private void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(780, 500);
	}

	private void setPosition() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);
	}

	void setLAF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			JOptionPane.showOptionDialog(this, e.getMessage(), "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, "OK");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void initComponents() {
		JPanel p = new JPanel(new MigLayout());

		JPanel up = new JPanel(new MigLayout());
		JPanel down = new JPanel(new MigLayout());
		DefaultListModel<File> model = new DefaultListModel<File>();
		JList<File> fileList = new JList<File>(model) {

			private static final long serialVersionUID = 2506194711677741791L;

			@Override
			public String getToolTipText(MouseEvent evt) {
				int index = locationToIndex(evt.getPoint());
				File item = getModel().getElementAt(index);
				return item.toString();
			}

		};
		JScrollPane fileListScrollPane = new JScrollPane(fileList);
		JButton openFolder = new JButton("Find training data");
		JButton removeFile = new JButton("Remove file");
		JButton saveData = new JButton("Save training data to file");
		JButton loadData = new JButton("Load training data from file");
		JButton startTraining = new JButton("Start training");

		Font labelFont = new Font("Arial", Font.PLAIN, 15);
		JLabel recDig = new JLabel("Recognized digit: ");
		final JLabel digit = new JLabel();
		recDig.setFont(labelFont);
		digit.setFont(labelFont);

		JButton recordButton = new JButton("Record");
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
		up.add(fileListScrollPane, "span 1 5, height 200:200:200, width 500:500:500");
		up.add(openFolder, "wrap");
		up.add(removeFile, "wrap");
		up.add(saveData, "wrap");
		up.add(loadData, "wrap 10px");
		up.add(startTraining);

		down.setBorder(BorderFactory.createTitledBorder("Recognition"));
		down.add(recordButton, "align center");
		down.add(openFile, "split 2");
		down.add(recognize, "wrap");
		down.add(progressBar, "wrap 15px");
		down.add(recDig, "split 2");
		down.add(digit);

		p.add(up, "span, height 250:250:250, width 750:750:750");
		p.add(down, "span, height 130:130:130, width 750:750:750");

		openFolder.addActionListener(new AproveOpenFolder(openFolderDialog, fileList));

		removeFile.addActionListener(new RemoveFileListener(fileList));

		saveData.addActionListener(new SaveToFileListener(fileList, saveFileDialog));

		loadData.addActionListener(new LoadFromFileListener(fileList, loadFileDialog));

		final RecordListener recordListener = new RecordListener(progressBar);
		recordButton.addActionListener(recordListener);

		progressBar.addChangeListener(new ProgressBarListener(progressBar));

		recognize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				byte[] recording = recordListener.getRecording();
				if (recording == null) {
					JOptionPane.showMessageDialog(MainFrame.this, "Nothing is recorded.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					digit.setText(String.valueOf(recognize(recording)));
				}
			}

		});
	}

	protected abstract int recognize(byte[] record);

	protected abstract void train(List<String> filenames);

}
