import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.proteanit.sql.DbUtils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class Interface extends JFrame {
	private static Object[][] columnData;
	private JPanel contentPane;
	private JPanel panel;
	private static JTable table;
	private final static String root = System.getProperty("user.home");
	private static Connection connection = sqliteConnection.dbConnector();
	private JTextField textField;
	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 * @throws SQLException 
	 */
	public static void refreshTable() {
		try {
			String query = "SELECT rowid, date,name,number FROM CellCount ORDER BY date";
			PreparedStatement prt = connection.prepareStatement(query);
			ResultSet rs = prt.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			prt.close();
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws IOException, SQLException  {	

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
		setBounds(100, 100, 722, 522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton buttonDisplay = new JButton("Upload File");
		buttonDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		buttonDisplay.setBounds(104, 40, 114, 35);
		contentPane.add(buttonDisplay);

		panel = new JPanel();
		panel.setBounds(65, 124, 423, 311);
		contentPane.add(panel);
		panel.setLayout(null);


		JLabel lblWorkHistory = new JLabel("Work History");
		lblWorkHistory.setBounds(29, 13, 124, 16);
		panel.add(lblWorkHistory);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 42, 400, 256);
		
		scrollPane.getViewport();
		panel.add(scrollPane);

		table = new JTable();

		
		
		
		int col=table.getSelectedColumn();
		int row=table.getSelectedRow();
		//do the update query on this row
		
		
		
		scrollPane.setViewportView(table);
		refreshTable();
		
		//String[] microscopeTypes = {"optic", "fluoroscent", "TEM"};
		
		JButton btnSaveChanges = new JButton("Save Changes");
		btnSaveChanges.setBounds(515, 40, 134, 35);
		btnSaveChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int col=table.getSelectedColumn();
				int row=table.getSelectedRow();
				
				//do the update query on this row
				  
				try {
				           PreparedStatement ps = connection.prepareStatement("UPDATE CellCount SET number = " + textField.getText() + " WHERE rowid = "+(row+1));

				            ps.executeUpdate(); 
				            ps.close();
				            refreshTable();
				           
				        } catch (Exception ex) {
				          ex.printStackTrace();
				        }
			}
		});
		contentPane.add(btnSaveChanges);
		
		textField = new JTextField();
		textField.setBounds(513, 154, 116, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnCreateGraph = new JButton("Create Graph");
		btnCreateGraph.setBounds(290, 40, 129, 35);
		contentPane.add(btnCreateGraph);
		btnCreateGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int[] indexes = table.getSelectedRows();
				System.out.println(indexes[0] + "\n" + indexes[1]);
				List<Double> graphValues = new ArrayList();
				for(int i = 0; i < indexes.length; i++) {
					String graphQuery = "SELECT number FROM CellCount WHERE rowid= " + (indexes[i] + 1);
					try {
						PreparedStatement ps = connection.prepareStatement(graphQuery);
						ResultSet rs = ps.executeQuery();
						while(rs.next()) {
							graphValues.add(rs.getDouble(1));
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				GraphPanel gp = new GraphPanel(graphValues);
				gp.createAndShowGui(graphValues);
				
			}
			
		});

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
						
						String[] options = {"Count and Save", "Discard"};
				        int x = JOptionPane.showOptionDialog(null, "",
				                "Click a button",
				                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				        System.out.println(x);
				        if(x == 0) {
				        	
				        	ProcessBuilder pb = new ProcessBuilder("C:\\Python27\\python.exe", "C:\\Python27\\LengthAndCount.py",path, path.concat("_out.jpg"));
							Process p = pb.start();
							
							Pattern pattern = Pattern.compile(".*\\\\s*(.*)");
							Matcher matcher = pattern.matcher(path);
							System.out.println();
							String fileName = "";
							if (matcher.find())
								fileName = matcher.group(1);
							String insertQuery = "INSERT INTO CellCount (date, name, number) VALUES (?,?,?)";

						
							
							BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String s = null;
							int cellCount  = -1;
							while((s= in.readLine())!= null) {
								System.out.println(s);
								cellCount++;
							}
							
							FileWriter writer = new FileWriter(root + "\\Documents\\CountingCells\\history.txt", true);
							DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
							Date date = new Date();
							String dateString = date.toString();
							try {
								PreparedStatement prt = connection.prepareStatement(insertQuery);
								prt.setString(1, dateString);
								prt.setString(2, fileName);
								prt.setString(3, Integer.toString(cellCount));
								prt.execute();
								prt.close();
							}
							catch(Exception k){
								k.printStackTrace();
							}
							
							
							writer.write(dateFormat.format(date) + "," + cellCount + "\n");
							writer.flush();
							System.out.println(cellCount);
							refreshTable();
				        }

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
