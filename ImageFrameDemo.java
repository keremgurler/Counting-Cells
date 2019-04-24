
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This is a test program that draws an image provided by the user and scales
 * the image to fit its parent container (a JLabel).
 * 
 * @author www.codejava.net
 *
 */
public class ImageFrameDemo extends JFrame implements ActionListener {
	private JButton buttonDisplay = new JButton("Upload");
	private JLabel labelImage = new ScaledImageLabel();

	public ImageFrameDemo() {
		super("Image Frame Demo");

		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTHWEST;

		constraints.gridy = 0;
		constraints.gridx = 0;

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;

		constraints.gridx = 1;

		constraints.gridx = 2;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.0;
		add(buttonDisplay, constraints);

		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		constraints.gridy = 1;
		constraints.gridx = 0;
		constraints.gridwidth = 3;
		labelImage.setPreferredSize(new Dimension(400, 300));
		add(labelImage, constraints);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);

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
						labelImage.setIcon(new ImageIcon(image));
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ImageFrameDemo().setVisible(true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}