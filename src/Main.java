import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Main {
	
	private static JFrame GUI(String title, Dimension size, boolean resizable){
		
		// Define the frame
		JFrame frame;
		
		// Set the general proporties of jframe
		frame = new JFrame();
		frame.setSize(size);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);
		frame.setResizable(resizable);
		return frame;
	}
	
	public static void main(String[] args){
		
		// Initialize the necessary tools
		OSUIntelligenceTools OSU = new OSUIntelligenceTools();
		JFrame frame = GUI("Open Source Intelligence", new Dimension(500,600), false);
		
		// Define components
		JPanel panel = new JPanel();
		JTextField compNameT = new JTextField(), compWebsiteT = new JTextField();
		JTextArea screen = new JTextArea();
		JLabel compNameL = new JLabel("Enter company name: "),compWebsiteL = new JLabel("Enter company website: ");
		JButton submit = new JButton("Submit");
		screen.setEditable(true);
		JScrollPane fullScreen = new JScrollPane(screen);
		
		// Adjust properties of components
		panel.setBackground(Color.GRAY);
		fullScreen.setPreferredSize(new Dimension(450,450));
		compNameT.setPreferredSize(new Dimension(300,25));
		compWebsiteT.setPreferredSize(new Dimension(300,25));
		submit.setSize(new Dimension(80,20));
		submit.addActionListener(new ActionListener() {

			// Add an actionListener to the submit button
			public void actionPerformed(ActionEvent e) {
            	
            	// Get the input variables and clear the screen
            	String name = compNameT.getText(), website = compWebsiteT.getText();
            	
				// Create the file for the program to write the results and this action needs to be in a try block due to the IOException
				try {
					BufferedWriter file = new BufferedWriter(new FileWriter("Open Source Intelligence Gathering on " + name + ".docx"));
					StringBuilder screenText = new StringBuilder();
					
					// Edgar step
	            	String edgar = OSU.getEdgar(name);
	            	screenText.append(edgar + "\n\n");
	            	file.write(edgar + "\n\n");
	            	
	            	// Privacy Rights step
	            	String privacyRights = OSU.getPrivacyRights(name);
	            	screenText.append(privacyRights + "\n\n");
	            	file.write(privacyRights + "\n\n");
	            	
	            	// Robtex step
	            	String robtext[] = OSU.getRobtex(website);
	            	for(int i = 0; i < robtext.length; i++) {
	            		screenText.append(robtext[i] + "\n");
	            		file.write(robtext[i] + "\n");
	            	}
	            	
	            	// Whois step
	            	screenText.append("\n");
	            	file.write("\n");
	            	try {
						String whois[] = OSU.getWhois(website);
		            	for(int i = 0; i < whois.length; i++) {
		            		screenText.append(whois[i] + "\n");
		            		file.write(whois[i] + "\n");
		            	}
		            	
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            	
	            	// Set the screen
	            	screen.setText(screenText.toString());
	            	
	            	// The SSLlabs step
	            	try {
						OSU.openSSLLABSbrowser(website);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
	            	
	            	// Clear the inputs and close the BufferedWriter
	            	compNameT.setText("");
	            	compWebsiteT.setText("");
	            	file.close();
	            	
				} catch (IOException e2) {
					JOptionPane.showMessageDialog(new JFrame("Error"), "A crucial error has occured. Program exiting");
					System.exit(1);
				}
            }
        });
		
		// Add the components
		frame.getContentPane().add(panel);
		panel.add(compNameL);
		panel.add(compNameT);
		panel.add(compWebsiteL);
		panel.add(compWebsiteT);
		panel.add(submit);
		panel.add(fullScreen);
		
		// Finish the frame
		frame.setVisible(true);
		
	}
}
