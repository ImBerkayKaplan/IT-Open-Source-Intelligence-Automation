public class Main {
	
	public static void main(String[] args) {
		
		// Initialize the necessary tools
		OSUIntelligenceTools OSU = new OSUIntelligenceTools();
		
		// Get the company name as index 1 and website as index 0
		String info[] = OSU.getInfo();
		
		// Just a line seperator
		System.out.println();
		
		// Hoovers step
		System.out.println("Started searching on www.hoovers.com");
		System.out.println(OSU.getHoovers(info[1]) + "\n");
		
		// Edgar step
		System.out.println("Started searching on Edgar www.sec.gov");
		System.out.println(OSU.getEdgar(info[1]) + "\n");
		
		// Privacy Rights step
		System.out.println("Started searching on privacyrights.org.");
		System.out.println(OSU.getPrivacyRights(info[1]) + "\n");
		
		// Whois step
		System.out.println("Started searching on whois.domaintools.com. If step fails, make sure you did not reach your lookup limit in whois.domaintools.com.");
		System.out.println(OSU.getWhoIs(info[0])[0] + "\n");
		
	}

}
