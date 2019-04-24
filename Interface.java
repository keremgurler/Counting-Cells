import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class Interface extends JFrame {
	private static Object[][] columnData;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		File historyFile = new File(System.getProperty("user.home") + "\\history.csv");

		try {
			historyFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {

			BufferedReader reader = new BufferedReader(new FileReader(historyFile));
			String line = reader.readLine();
			Path path = Paths.get(System.getProperty("user.home") + "\\history.csv");
			long lineCount = Files.lines(path).count();
			columnData = new Object[(int) lineCount][2];
			int i = 0;
			while (line != null) {

				columnData[i][0] = line.split(",")[0];
				columnData[i][1] = Integer.parseInt(line.split(",")[1]);
				line = reader.readLine();
				i++;
			}
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Interface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1056, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton buttonDisplay = new JButton("Upload File");
		buttonDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		buttonDisplay.setBounds(61, 24, 97, 25);
		contentPane.add(buttonDisplay);
		String[] listItems1 = { "Coccus", "Spiral", "Bacillus" };
		JComboBox comboBox1 = new JComboBox(listItems1);
		comboBox1.setBounds(435, 24, 113, 25);
		contentPane.add(comboBox1);

		String[] listItems2 = { "Length", "Count" };
		JComboBox comboBox2 = new JComboBox(listItems2);
		comboBox2.setBounds(778, 25, 108, 25);
		contentPane.add(comboBox2);

		JLabel lblShapeOfCells = new JLabel("Shape of Cells");
		lblShapeOfCells.setBounds(307, 24, 90, 25);
		contentPane.add(lblShapeOfCells);

		JLabel lblLengthOrCount = new JLabel("Length or Count");
		lblLengthOrCount.setBounds(634, 25, 97, 23);
		contentPane.add(lblLengthOrCount);

		String[] HistoryList = { "1st experiment", "2nd experiment", "3rd experiment", "4th experiment" };

		JPanel panel = new JPanel();
		panel.setBounds(61, 124, 423, 311);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblWorkHistory = new JLabel("Work History");
		lblWorkHistory.setBounds(29, 13, 124, 16);
		panel.add(lblWorkHistory);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 42, 399, 256);
		panel.add(scrollPane);

		String[] columnNames = { "Date", "Count" };
		JTable workHistory = new JTable(columnData, columnNames);
		scrollPane.setViewportView(workHistory);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(563, 124, 423, 311);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		Canvas canvas = new Canvas();
		canvas.setBounds(10, 10, 276, 198);
		panel_1.add(canvas);

		buttonDisplay.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));
				// filter the files
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
				file.addChoosableFileFilter(filter);
				int result = file.showSaveDialog(null);
				// if the user click on save in Jfilechooser
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = file.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					try {
						Image image = ImageIO.read(new File(path));
						JDialog imageDialog = new JDialog();
						imageDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
						imageDialog.setTitle("image");
						JLabel imgLabel = new ScaledImageLabel();

						imgLabel.setPreferredSize(new Dimension(800, 600));

						imgLabel.setIcon(new ImageIcon(image));

						imageDialog.getContentPane().add(imgLabel);
						imageDialog.pack();
						imageDialog.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				// if the user click on save in Jfilechooser

				else if (result == JFileChooser.CANCEL_OPTION) {
					System.out.println("No File Select");
				}
			}
		});
	}
}
