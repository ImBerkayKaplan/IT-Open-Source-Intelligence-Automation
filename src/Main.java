import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	
	public static void main(String[] args) {
		
		// Initialize the necessary tools
		OSUIntelligenceTools OSU = new OSUIntelligenceTools();
		JFrame frame = GUI("Open Source Intelligence", new Dimension(500,400), false);
		
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
		fullScreen.setPreferredSize(new Dimension(450,250));
		compNameT.setPreferredSize(new Dimension(300,25));
		compWebsiteT.setPreferredSize(new Dimension(300,25));
		submit.setSize(new Dimension(80,20));
		submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	// Get the input variables and clear the screen
            	String name = compNameT.getText(), website = compWebsiteT.getText();
            	screen.setText("");
            	
            	// Edgar step
            	screen.setText(screen.getText() + "Started searching on Edgar www.sec.gov\n");
            	screen.setText(screen.getText() + OSU.getEdgar(name) + "\n\n");
            	
            	// Privacy Rights step
            	screen.setText(screen.getText() + "Started searching on privacyrights.org.\n");
            	screen.setText(screen.getText() + OSU.getPrivacyRights(name) + "\n\n");
            	
            	// Robtex step
            	screen.setText(screen.getText() + "Started searching on www.robtex.com.\n");
            	String robtext[] = OSU.getRobtex(website);
            	for(int i = 0; i < robtext.length; i++) {
            		screen.setText(screen.getText() + robtext[i] + "\n");
            	}
            	//screen.setText(screen.getText() + "\n");
            	
            	// Clear the inputs
            	compNameT.setText("");
            	compWebsiteT.setText("");
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
