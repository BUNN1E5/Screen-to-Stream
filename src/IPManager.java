import java.io.*;
import java.net.*;

public class IPManager {
	
	public static String getExternalIP()
	{
		URL whatismyip;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
	                whatismyip.openStream()));

			String ip = in.readLine(); //you get the IP as a String
			return ip;
		} catch (Exception e) {
			return "";
		}
	}

}
