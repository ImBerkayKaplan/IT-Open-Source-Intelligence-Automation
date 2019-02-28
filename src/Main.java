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
		JLabel compNameL = new JLabel("Enter company name: "),compWebsiteL = new JLabel("Enter company website: "), warning = new JLabel("Please allow 30 seconds");
		JButton submit = new JButton("Submit");
		screen.setEditable(true);
		JScrollPane fullScreen = new JScrollPane(screen);
		
		// Adjust properties of components
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
					
					// Create the file and the text for the screen
					BufferedWriter file = new BufferedWriter(new FileWriter("Open Source Intelligence Gathering on " + name + ".rtf"));
					file.write("Risk Assessment on " + name + "\n\n1 a) - Google Finance:\n1 b) - Hoovers:\n1 c) - Edgar:\n");
					StringBuilder screenText = new StringBuilder();
					
					// Edgar step
	            	String edgar = OSU.getEdgar(name);
	            	screenText.append(edgar + "\n\n");
	            	file.write(edgar + "\n\n");
	            	
	            	// Privacy Rights step
	            	file.write("2 - Privacy Rights ClearingHouse Breach History:\n");
	            	String privacyRights = OSU.getPrivacyRights(name);
	            	screenText.append(privacyRights + "\n\n");
	            	file.write(privacyRights + "\n\n");
	            	
	            	
	            	
	            	// Whois.domaintools.com step
	            	file.write("3 - Whois Domain Tools for IP address, physical location, and ASN: \n");
	            	try {
						String whois[] = OSU.getWhois(website);
		            	for(int i = 0; i < whois.length; i++) {
		            		screenText.append(whois[i] + "\n");
		            		file.write(whois[i] + "\n");
		            	}
		            	
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            	
	            	// Robtex step
	            	screenText.append("\n");
	            	file.write("\n4 - Robtex.com: \n");
	            	String robtext[] = OSU.getRobtex(website);
	            	for(int i = 0; i < robtext.length; i++) {
	            		screenText.append(robtext[i] + "\n");
	            		file.write(robtext[i] + "\n");
	            	}
	            	
	            	// Set the screen
	            	file.write("\n5 - Hurricane Electric https://bgp.he.net/AS####\n\n6 - Query their DNS servers with www.ultratools.com/tools/zoneFileDump (If zone transfer fails, that is secure):\n\n7 - Validate that their SSL/TLS configuration is secure (It should only support TLS 1.1, 1.2)\n\n8 - Check shodan with www.shodan.io\n\n9 - If they have a mobile application, downlaod and analyze their mobile application");
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
	            	
				} catch (Exception e2) {
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
		panel.add(warning);
		
		// Finish the frame
		frame.setVisible(true);
		
	}
}
