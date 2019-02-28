import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OSUIntelligenceTools {
	
	
	// Predetermined constants for the structure of OSUIntelligenceTools
	private static final String ERROR_MESSAGE = "Step failed due to a technical error. Contact the programmer please.";
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
			Document doc = Jsoup.connect(website).timeout(5000).get();
					
			// Check if any information exists by seeing if the specific class exists
			if(!doc.getElementsByClass("noCompanyMatch").isEmpty()) {
				result =  "No information found on Edgar.";
			}else {
				result = "Information detected on Edgar. Go to " + website;
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
			Document doc = Jsoup.connect(website).timeout(5000).get();
			
			// Get the specific element to make sure to breaches are detected
			Elements breach = doc.select(".field-content");
			
			// Check if any breaches exist
			if(breach.get(0).childNode(0).toString().equals("0")) {
				result = "No information found on Privacy Rights.";
			}else {
				result = "Breach detected on Privacy Rights. Go to " + website;
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
			Document doc = Jsoup.connect(website).timeout(5000).get();
			
			// Get all the necessary rows in the page and convert to nodes
			Elements elements = doc.select(".table2col td");
			
			// Go through all elements
			for(int i = 0; i < elements.size(); i++) {
				Element element = elements.get(i);
				
				// Put the header to the result. It will be followed by the data.
				result[i] = element.parent().child(0).text() + ": " + element.text();
			}

		}catch(Exception e) {
			
			// Set all elements the error message
			for(int i = 0; i < NUMBER_OF_ROBTEXELEMENTS; i++) {
				result[i] = ERROR_MESSAGE;
			}
		}
		
		// Just a customized error message whe an intended information was not found on whois.domaintools.com
		for(int j = 0; j < result.length; j++) {
			if(result[j].equals(null)) {
				result[j] = "This information was not found on robtex.com";
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
		Document whoisResponse = proxy.proxyStream("http://whois.domaintools.com/" , website);
		Elements rows = new Elements();
		
		// If the response is null, then all the proxies have reached their whois.com usage limit. You may wait for a while, or use other proxies
		if(whoisResponse != null) {
			rows = whoisResponse.getElementsByClass("row-label");
			
			// Add the IP Location, Server Type, and ASN to the result by iterating through all rows to find those specific elements
			int counter1 = 0;
			for(int i = 0; i < rows.size(); i++) {
				if(rows.get(i).text().equals("IP Location") || rows.get(i).text().equals("Server Type") || rows.get(i).text().equals("ASN")) {
					result[counter1++] = rows.get(i).text() + ": " +rows.get(i).parent().child(1).text();
				}
			}
			
		}else {
			
			// Set all elements the error message
			for(int i = 0; i < NUMBER_OF_WHOISELEMENTS; i++) {
				result[i] = ERROR_MESSAGE;
			}
		}
		
		// Just a customized error message whe an intended information was not found on whois.domaintools.com
		for(int j = 0; j < result.length; j++) {
			if(result[j].equals(null)) {
				result[j] = "This information was not found on whois.domaintools.com";
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
}
