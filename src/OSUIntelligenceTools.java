import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class OSUIntelligenceTools {
	
	public static final String ERROR_MESSAGE = "Step failed due to a technical error. Contact the programmer please.";
	public static final String NO_INFORMATION_MESSAGE = "No information found. Please proceed";
	public static final int NUMBER_OF_ROBTEXELEMENTS = 8;
	
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
	
	/* This method checks the information on http://whois.domaintools.com/
	 * 
	 * @params websiteName
	 * 		The website name in question
	 * 
	 * @returns The information found on whois as String array
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
			for(int i = 0; i < result.length; i++) {
				result[i] = ERROR_MESSAGE;
			}
		}
				
		// Return the Privacy Rights breaches
		return result;
		
	}
	
	/* Work in progress
	String getUltraTools(String websiteName) {
		
		// Initalize the result
		String result = "Method not completed yet";
		String website = "https://www.ultratools.com/tools/zoneFileDump";
		
		try {
			
			Connection conn = Jsoup.connect(website);
			Connection.Response res = conn.method(Method.GET).execute();
			Document docGet = res.parse();
			
			
			String as_fid = docGet.getElementsByAttributeValue("name", "as_fid").attr("value");
			String as_sfid = docGet.getElementsByAttributeValue("name", "as_sfid").attr("value");
			
			Connection.Response resPos = conn.data("zoneName", websiteName).data("as_sfid", as_sfid).data("as_fid", as_fid).cookies(res.cookies()).method(Method.POST).execute();
			
			
			System.out.println(resPos.parse().getElementsByClass("error").toString());
		} catch (IOException e) {
			
		}

		
		return result;
	}
	*/
	
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
