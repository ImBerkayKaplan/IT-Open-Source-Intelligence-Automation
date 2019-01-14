import java.util.Iterator;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OSUIntelligenceTools {
	
	public static final String ERROR_MESSAGE = "Step failed due to a technical error. Contact the programmer please.";
	public static final String NO_INFORMATION_MESSAGE = "No information found. Please proceed";
	public static final int NUMBER_OF_WHOISELEMENTS = 4;
	
	/*
	 * TODO: complete method header
	 */
	String[] getInfo() {
		
		// Initialize the result and the Scanner to get input and return
		String result[] = new String[2];
		Scanner in = new Scanner(System.in);
		
		// Get the result from the user and close the scanner
		System.out.println("Enter the company website please");
		result[0] = in.nextLine();
		System.out.println("Enter the company name please");
		result[1] = in.nextLine();
		in.close();
		
		// Return the company website as the first element and the company name as the second element
		return result;
	}
	
	/*
	 * TODO: Complete method header
	 */
	String getHoovers(String name) {
		
		// Initalize the result
		String result = "Method not completed yet";
		
		try {
			
			// Get the page
			String website = "http://www.hoovers.com/company-information/company-search.html?term=" + name;
			Document doc = Jsoup.connect(website).get();
					
			// Check if any information exists by seeing if the specific class exists
			if(!doc.getElementsByClass("company_name").isEmpty()) {
				result = "Information detected. Go to " + website;
			}else {
				result =  NO_INFORMATION_MESSAGE;
			}
					
		}catch(Exception e) {
			
			// Print error message
			//System.out.println(e);
			result = ERROR_MESSAGE;
		}
		
		// Return the Hoovers information
		return result;
	}
	
	/* This method checks if any information about the company exist on Edgar and returns the appropriate message.
	 * 
	 * @requires name
	 * 		The company name in question
	 * 
	 * @returns result
	 * 		The message that indicates if anything exist on Edgar owned by www.sec.gov
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
			
			// Print error message
			//System.out.println(e);
			result = ERROR_MESSAGE;
		}
				
		// Return the Edgar information
		return result;
	}
	
	/* This method checks if the company has any breaches on www.privacyrights.org and returns the appropriate message.
	 * 
	 * @requires name
	 * 		The company name in question
	 * 
	 * @returns result
	 * 		The message that indicates if anything exist on PrivacyRights
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
			
			// Print error message
			//System.out.println(e);
			result = ERROR_MESSAGE;
		}
		
		// Return the Privacy Rights breaches
		return result;
	}
	
	/* This method checks the information on http://whois.domaintools.com/
	 * 
	 * @requires websiteName
	 * 		The website name in question
	 * 
	 * @returns result
	 * 		The information found on whois as String array
	 */
	String[] getWhoIs(String websiteName) {
		
		// Initalize the result
		String[] result = new String[NUMBER_OF_WHOISELEMENTS];
				
		try {
					
			// Get the page
			String website = "http://whois.domaintools.com/" + websiteName;
			Document doc = Jsoup.connect(website).get();
			
			// Get all the necessary rows in the page
			Elements elements= doc.getElementsByClass("row-label");
			
			// Set up the iterator and insert the worst case messages
			Iterator<Element> it = elements.iterator();
			result[0] = "IP address could not be found";
			result[1] = "Server Location unkown";
			result[2] = "Server host if third party unkown";
			result[3] = "Server type unkown";
			
			while(it.hasNext()) {
				System.out.println(it.next());
			};

		}catch(Exception e) {
			
			// Print error message
			//System.out.println(e);
			
			// Set all elements the error message
			for(int i = 0; i < result.length; i++) {
				result[i] = ERROR_MESSAGE;
			}
		}
				
		// Return the Privacy Rights breaches
		return result;
		
	}
	
}
