package gui;

import gui.listeners.AproveOpenFolder;
import gui.listeners.LoadFromFileListener;
import gui.listeners.OpenRecordFromFileListener;
import gui.listeners.RecordListener;
import gui.listeners.RemoveFileListener;
import gui.listeners.SaveToFileListener;
import gui.listeners.StartTrainingListener;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author igorletso
 * @author niktrk
 * 
 */
public abstract class MainFrame extends JFrame {

	private static final long serialVersionUID = -3838320390904637165L;
	private JProgressBar trainingProgress;

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
		setSize(780, 400);
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
		final JList<File> fileList = new JList<File>(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public String getToolTipText(MouseEvent evt) {
				int index = locationToIndex(evt.getPoint());
				if (index > -1) {
					File item = getModel().getElementAt(index);
					return item.toString();
				} else {
					return null;
				}
			}
		};
		JScrollPane fileListScrollPane = new JScrollPane(fileList);
		JButton openFolder = new JButton("Find training data");
		JButton removeFile = new JButton("Remove file");
		JButton saveData = new JButton("Save training data to file");
		JButton loadData = new JButton("Load training data from file");
		JButton trainNetwork = new JButton("Train network");
		JButton saveSom = new JButton("Save network to file");
		JButton loadSom = new JButton("Load network from file");

		Font labelFont = new Font("Arial", Font.PLAIN, 15);
		JLabel recDig = new JLabel("Recognized digit: ");
		Font digitFont = new Font("Arial", Font.BOLD, 25);
		final JLabel digit = new JLabel();
		recDig.setFont(labelFont);
		digit.setFont(digitFont);

		JButton recordButton = new JButton("Record");
		JButton openFile = new JButton("Open from file");
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);

		JFileChooser openFolderDialog = new JFileChooser();
		openFolderDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JFileChooser saveFileDialog = new JFileChooser();
		JFileChooser loadFileDialog = new JFileChooser();
		FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text files", "txt");
		loadFileDialog.addChoosableFileFilter(txtFilter);
		loadFileDialog.setFileFilter(txtFilter);

		final JFileChooser saveSomDialog = new JFileChooser();
		saveSomDialog.addChoosableFileFilter(txtFilter);
		saveSomDialog.setFileFilter(txtFilter);

		final JFileChooser loadSomDialog = new JFileChooser();
		loadSomDialog.addChoosableFileFilter(txtFilter);
		loadSomDialog.setFileFilter(txtFilter);

		JFileChooser openWavFileDialog = new JFileChooser();
		FileNameExtensionFilter wavFilter = new FileNameExtensionFilter("Wave files", "wav");
		openWavFileDialog.addChoosableFileFilter(wavFilter);
		openWavFileDialog.setFileFilter(wavFilter);

		Container cp = getContentPane();
		cp.add(p);

		up.setBorder(BorderFactory.createTitledBorder("Training"));
		up.add(fileListScrollPane, "span 1 7, height 200:200:200, width 500:500:500");
		up.add(openFolder, "wrap 4");
		up.add(removeFile, "wrap 4");
		up.add(saveData, "wrap 4");
		up.add(loadData, "wrap 22px");
		up.add(trainNetwork, "wrap 4");
		up.add(saveSom, "wrap 4");
		up.add(loadSom);

		down.setBorder(BorderFactory.createTitledBorder("Recognition"));
		down.add(recordButton, "align center");
		down.add(openFile, "split 2");
		down.add(recDig, "gapleft 150, split 2");
		down.add(digit, "wrap");
		down.add(progressBar);

		p.add(up, "span, height 250:250:250, width 750:750:750");
		p.add(down, "span, height 90:90:90, width 750:750:750");

		JDialog trainingDialog = new JDialog(this);
		trainingDialog.setTitle("Start training");
		trainingDialog.setModal(true);
		trainingDialog.setSize(445, 200);
		trainingDialog.setLocationRelativeTo(this);
		JPanel trainingPanel = new JPanel(new MigLayout());
		JButton trainSom = new JButton("Start training");
		final JSpinner numIteration = new JSpinner();
		trainingProgress = new JProgressBar();
		JLabel numIterationLabel = new JLabel("Enter the number of iterations:");
		trainingPanel.add(numIterationLabel, "gapleft 25, gaptop 50");
		trainingPanel.add(numIteration, "width 50:50:50");
		trainingPanel.add(trainSom, "gapleft 50");
		trainingPanel.add(trainingProgress, "gapleft 25, gaptop 20 ,cell 0 4 4 4, width 350:350:350");
		trainingDialog.add(trainingPanel);

		openFolder.addActionListener(new AproveOpenFolder(openFolderDialog, fileList));

		removeFile.addActionListener(new RemoveFileListener(fileList));

		saveData.addActionListener(new SaveToFileListener(fileList, saveFileDialog));

		loadData.addActionListener(new LoadFromFileListener(fileList, loadFileDialog));

		trainNetwork.addActionListener(new StartTrainingListener(trainingDialog));

		openFile.addActionListener(new OpenRecordFromFileListener(openWavFileDialog) {
			@Override
			protected void onRecordLoaded(byte[] record) {
				digit.setText(recognize(record));
			}
		});

		recordButton.addActionListener(new RecordListener(progressBar) {

			@Override
			protected void onRecordingDone(byte[] recording) {
				digit.setText(recognize(recording));
			}

		});

		trainSom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> filenames = new ArrayList<String>();
				ListModel<File> fileListModel = fileList.getModel();
				for (int i = 0; i < fileListModel.getSize(); i++) {
					filenames.add(fileListModel.getElementAt(i).getAbsolutePath());
				}
				trainingProgress.setMinimum(0);
				trainingProgress.setMaximum((int) numIteration.getValue());

				SwingWorker<Void, Void> sw = createSwingWorker(filenames, (int) numIteration.getValue());
				sw.execute();

			}

		});

		saveSom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveSomDialog.showSaveDialog(saveSomDialog.getParent());
				if (saveSomDialog.getSelectedFile() != null) {
					saveSomToFile(saveSomDialog.getSelectedFile());
				}
			}
		});

		loadSom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadSomDialog.showOpenDialog(loadSomDialog.getParent());
				if (loadSomDialog.getSelectedFile() != null) {
					loadSomFromFile(loadSomDialog.getSelectedFile());
				}
			}
		});

	}

	protected SwingWorker<Void, Void> createSwingWorker(final List<String> filenames, final int numIterations) {
		return new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				train(filenames, numIterations);
				return null;
			}

		};
	}

	protected abstract String recognize(byte[] record);

	protected abstract void train(List<String> filenames, int numIterations);

	protected abstract void saveSomToFile(File file);

	protected abstract void loadSomFromFile(File file);

	public void setProgress(int n) {
		trainingProgress.setValue(n);
	}

}
