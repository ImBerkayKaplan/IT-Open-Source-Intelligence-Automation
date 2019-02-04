import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/*
 * Some website block IP addresses after the user passes the limit get request. We will use proxy
 * that is gathered from another website to circumvent this issue. 
 */
public class ProxyTools {
	
	private Proxy proxy; // The proxy of this class that will be used to connect to a particular web page
	private HttpURLConnection connection; // The active connection
	private ArrayList<String> proxyIPs; // The list of the proxy IPs
	private ArrayList<Integer> proxyPorts; // The list of the proxy port numbers
	private String RESOURCE_URL; // The web page FQDN where the proxies will be gathered
	//private int index; // The index of the proxies to switch to other proxies when needed
	
	/*
	 * Initialize the list for the proxies' index, the resource_url for all proxy information, 
	 * get the proxy list, and initialize connection
	 */
	ProxyTools() throws IOException{
		
		
		// Initialize the list of proxies
		this.RESOURCE_URL = "https://www.us-proxy.org";
		this.proxyIPs = new ArrayList<String>();
		this.proxyPorts = new ArrayList<Integer>();
		//this.index = 0;
		initializeProxies();
		
		// Initalize the connection with the proxy
		//this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyIPs.get(this.index), this.proxyPorts.get(this.index)));
		//this.index++;
	}
	
	public void connect(String website) {
		
		// Define the URL, and connect to the web page using the proxy
		try {
			URL url = new URL(website);
			this.connection = (HttpURLConnection)url.openConnection(proxy);
			this.connection.connect();
		} catch (MalformedURLException e) {
			System.err.println("Wrong URL. Program exiting");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
		
		System.out.println(rows.toString());
		
	}
	
	public void intializeCon(String website){
		
		

	}
	
}
