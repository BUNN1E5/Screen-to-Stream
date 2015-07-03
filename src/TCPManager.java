import java.io.*;
import java.net.*;


public class TCPManager {
	
	Socket socket;
	String IP;
	int port;
	
	TCPManager(String IP, int port)
	{
		this.port = port;
		this.IP = IP;
	}
	
	public void writeMessage(String sendData)
	{
		try {
			socket = new Socket(IP , port);
			DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
			stream.writeBytes(sendData);
			socket.close();
		} catch (IOException e) {
		}
	}
	
	public String receiveMessage()
	{
		try {
			socket = new Socket(IP, port);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.close();
			return reader.readLine();
		} catch (Exception e) {
			return "";
		}
	}

}
