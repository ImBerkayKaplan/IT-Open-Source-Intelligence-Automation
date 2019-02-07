import java.util.Iterator;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OSUIntelligenceTools {
	
	
	// Predetermined constants for the structure of OSUIntelligenceTools
	private static final String ERROR_MESSAGE = "Step failed due to a technical error. Contact the programmer please.";
	private static final String NO_INFORMATION_MESSAGE = "No information found. Please proceed";
	private static final int NUMBER_OF_ROBTEXELEMENTS = 8;
	private static final int NUMBER_OF_WHOISELEMENTS = 3;
	
	/* This method checks if any information about the company exist on Edgar and returns the appropriate message.
	 * 
	 * @params name
	 * 		The company name in question
	 * 
	 * @returns The message that indicates if anything exist on Edgar owned by www.sec.gov
	 */
	String getEdgar(String name) {
		
		// Initalize the result
		String result = "Method not completed yet";
			
		
		try {
					
			// Get the page
			String website = "https://www.sec.gov/cgi-bin/browse-edgar?company="+name+"&owner=exclude&action=getcompany";
			Document doc = Jsoup.connect(website).get();
					
			// Check if any information exists by seeing if the specific class exists
			if(!doc.getElementsByClass("noCompanyMatch").isEmpty()) {
				result =  NO_INFORMATION_MESSAGE;
			}else {
				result = "Information detected. Go to " + website;
			}
					
		}catch(Exception e) {
			result = ERROR_MESSAGE;
		}
				
		// Return the Edgar information
		return result;
	}
	
	/* This method checks if the company has any breaches on www.privacyrights.org and returns the appropriate message.
	 * 
	 * @params name
	 * 		The company name in question
	 * 
	 * @returns The message that indicates if anything exist on PrivacyRights
	 */
	String getPrivacyRights(String name) {
		
		// Initalize the result
		String result = "Method not completed yet";
		
		try {
			
			// Get the page
			String website = "https://www.privacyrights.org/data-breaches?title=" + name;
			Document doc = Jsoup.connect(website).get();
			
			// Get the specific element to make sure to breaches are detected
			Elements breach = doc.select(".field-content");
			
			// Check if any breaches exist
			if(breach.get(0).childNode(0).toString().equals("0")) {
				result = NO_INFORMATION_MESSAGE;
			}else {
				result = "Breach detected. Go to " + website;
			}
			
		}catch(Exception e) {
			result = ERROR_MESSAGE;
		}
		
		// Return the Privacy Rights breaches
		return result;
	}
	
	/* This method checks the information on www.robtex.com
	 * 
	 * @params websiteName
	 * 		The website name in question
	 * 
	 * @returns The information found on robtex as String array
	 */
	String[] getRobtex(String websiteName) {
		
		// Initalize the result
		String[] result = new String[NUMBER_OF_ROBTEXELEMENTS];
				
		try {
					
			// Get the page
			String website = "https://www.robtex.com/dns-lookup/" + websiteName;
			Document doc = Jsoup.connect(website).get();
			
			// Get all the necessary rows in the page and convert to nodes
			Elements elements = doc.getElementsByClass("table2col");
			Iterator<Node> it = elements.first().child(1).childNodes().iterator();
			int i = 0;
			
			// Go through all element
			while(it.hasNext()) {
				Node node = it.next();
				
				// Make sure only the columns with data are taken into account
				if(node.childNodeSize() > 1) {
					
					// Put the header to the result. It will be followed by the data. Get the child of the second element which contains the data
					result[i] = node.childNode(0).childNode(0).toString() + ": ";
					Node child = node.childNode(1);
					
					// Get all data of the tree
					result[i] = result[i] + getJsoupTreeData(child);
					
					// Increment the counter to place the data correctly in result
					i++;
				}
			}

		}catch(Exception e) {
			
			// Set all elements the error message
			for(int i = 0; i < NUMBER_OF_ROBTEXELEMENTS; i++) {
				result[i] = ERROR_MESSAGE;
			}
		}
				
		// Return the Privacy Rights breaches
		return result;
		
	}
	
	/* This method checks the information on http://whois.domaintools.com/
	 * 
	 * @params websiteName
	 * 		The website name in question
	 * 
	 * @returns The information found on whois as String array
	 */
	String[] getWhois(String website) throws IOException{
		
		// Define the result array, the proxy tools to circumvent whois.com's limit usage, and get the website as a response
		String[] result = new String[NUMBER_OF_WHOISELEMENTS];
		ProxyTools proxy = new ProxyTools();
		Response whoisResponse = proxy.proxyStream("http://whois.domaintools.com/" , website);
		Elements rows = new Elements();
		
		// If the response is null, then all the proxies have reached their whois.com usage limit. You may wait for a while, or use other proxies
		if(whoisResponse != null) {
			rows = whoisResponse.parse().getElementsByClass("row-label");
			
			// Add the IP Location, Server Type, and ASN to the result by iterating through all rows to find those specific elements
			int counter1 = 0;
			for(int i = 0; i < rows.size(); i++) {
				if(rows.get(i).text().equals("IP Location")) {
					result[counter1] = rows.get(i).text() + ": " +rows.get(i).parent().child(1).text();
					counter1++;
				}
				if(rows.get(i).text().equals("Server Type")) {
					result[counter1] = rows.get(i).text() + ": " +rows.get(i).parent().child(1).text();
					counter1++;
				}
				if(rows.get(i).text().equals("ASN")) {
					result[counter1] = rows.get(i).text() + ": " +rows.get(i).parent().child(1).text();
					counter1++;
				}
			}
			
		}else {
			
			// Set all elements the error message
			for(int i = 0; i < NUMBER_OF_WHOISELEMENTS; i++) {
				result[i] = ERROR_MESSAGE;
			}
		}
		
		// Return the whois.domaintools.com information about the company
		return result;
	}
	
	/* This method opens a new web browser to the www.ssllabs.com
	 * 
	 * @params websiteName
	 * 		The website name in question
	 */
	void openSSLLABSbrowser(String website) throws IOException, URISyntaxException {
		
		// Check if the desktop is supported and open the browser
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    Desktop.getDesktop().browse(new URI("https://www.ssllabs.com/ssltest/analyze.html?d=" + website));
		}
	}
	
	/* A private internal method that gets all concatenated leaves of the Jsoup tree with a little customization for the robtex website
	 * 
	 * @params root
	 * 		The tree in Jsoup
	 * 
	 * @returns all concatenated leaves of root
	 */
	private String getJsoupTreeData(Node root) {
	
		String result = "";
		
		// Go through all child nodes
		for(int i = 0; i < root.childNodeSize(); i++) {
			
			// If the root still has children, go to the leftmost node, otherwise, get the data
			if(root.childNode(i).childNodeSize()>0) {
				
				// Concat the string to the data inside the child of root and a custom solution to prevent unwanted spaces appearing after the bold tag
				if(root.childNode(i).nodeName().equals("b")) {
					result = result + getJsoupTreeData(root.childNode(i));
				}else {
					result = result + getJsoupTreeData(root.childNode(i)) + " ";
				}
			}else {
				
				// A custom solution to prevent unwanted <ul>s appearing at the end of the string
				if(!root.childNode(i).nodeName().equals("ul")) {
					result = result + root.childNode(i).toString();
				}
			}
		}
		
		// Return the leaf
		return result;
	}
	
}
