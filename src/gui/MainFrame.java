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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
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

		Font labelFont = new Font("Arial", Font.PLAIN, 15);
		JLabel recDig = new JLabel("Recognized digit: ");
		final JLabel digit = new JLabel();
		recDig.setFont(labelFont);
		digit.setFont(labelFont);

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

		JFileChooser openWavFileDialog = new JFileChooser();
		FileNameExtensionFilter wavFilter = new FileNameExtensionFilter("Wave files", "wav");
		openWavFileDialog.addChoosableFileFilter(wavFilter);
		openWavFileDialog.setFileFilter(wavFilter);

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
		up.add(trainNetwork);

		down.setBorder(BorderFactory.createTitledBorder("Recognition"));
		down.add(recordButton, "align center");
		down.add(openFile, "split 2");
		down.add(progressBar, "wrap 15px");
		down.add(recDig, "split 2");
		down.add(digit);

		p.add(up, "span, height 250:250:250, width 750:750:750");
		p.add(down, "span, height 130:130:130, width 750:750:750");

		JDialog trainingDialog = new JDialog(this);
		trainingDialog.setTitle("Start training");
		trainingDialog.setModal(true);
		trainingDialog.setSize(440, 200);
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

	public void setProgress(int n) {
		trainingProgress.setValue(n);
	}

}
