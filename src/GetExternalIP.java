import java.io.*;
import java.net.*;

public class GetExternalIP {
	
	public static String getIP()
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
