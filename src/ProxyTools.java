import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
 * Some website block IP addresses after the user passes the limit get request. We will use proxy
 * that is gathered from another website to circumvent this issue. 
 */
public class ProxyTools {
	
	private ArrayList<String> proxyIPs; // The list of the proxy IPs
	private ArrayList<Integer> proxyPorts; // The list of the proxy port numbers
	private String RESOURCE_URL; // The web page FQDN where the proxies will be gathered
	private int index; // The index of the proxies to switch to other proxies when needed
	
	/*
	 * Initialize the list for the proxies' index, the resource_url for all proxy information, 
	 * get the proxy list, and initialize connection
	 */
	ProxyTools() throws IOException{
		
		// Initialize the list of proxies
		this.RESOURCE_URL = "https://www.us-proxy.org";
		this.proxyIPs = new ArrayList<String>();
		this.proxyPorts = new ArrayList<Integer>();
		this.index = 0;
		initializeProxies();
	}
	
	/*
	 * Attempt to access the resource using all the proxies in the list
	 * 
	 * @params resource
	 * 		The resource that will be accessed
	 * @params website
	 * 		The website that will be queried in the resource
	 * @returns result
	 * 		If the proxy could access the resource, it will return the response from the query, otherwise null
	 */
	Response proxyStream(String resource ,String website) throws IOException{
		
		// Initialize the status code and the response
		int status = 0;
		Response result = null;
		do{
			
			// Connect to the specific website using the proxy and enforce a timeout to avoid unactive proxies
			try {
				result = Jsoup.connect(resource + website).proxy(this.proxyIPs.get(this.index), this.proxyPorts.get(this.index)).timeout(5000).execute();
				status = result.statusCode();
			}catch(SocketTimeoutException e) {
				status = 0;
			}catch (HttpStatusException e) {
				status = 0;
			}
			
			// Increment the index to go to the next proxy
			this.index++;
            
			// Keep iterating while the status code is not success or the index did not exceed the size
		}while(status != 200 && this.index < this.proxyIPs.size());
		
		// Return the response, or null if the resource acess was unsuccessful
		return result;
	}
	
	private void initializeProxies() throws IOException{
		
		// Initalize the document that is produced from a get request to the resource
		Document doc = Jsoup.connect(this.RESOURCE_URL).get();
		
		// Get all rows of the proxies
		Elements rows = doc.getElementsByClass("table table-striped table-bordered").select("tr");
		
		// Remove the first and the last element to get rid of the header and the footer
		rows.remove(0);
		rows.remove(rows.size() - 1);
		
		// Add all proxy information to the global arrayList proxies
		for(int i = 0; i < rows.size(); i++){
			this.proxyIPs.add(rows.get(i).child(0).text());
			this.proxyPorts.add(Integer.parseInt(rows.get(i).child(1).text()));
		}
	}
}
