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
			stream.writeBytes(sendData + '\n');
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receiveMessage()
	{
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return reader.readLine();
		} catch (IOException e) {
			return "";
		}
	}

}
