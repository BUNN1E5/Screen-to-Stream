import java.io.*;
import java.net.*;

import sun.print.resources.serviceui;


public class TCPManager {
	
	Socket socket;
	ServerSocket receiveSocket;
	String IP;
	int port;
	
	TCPManager(String IP, int port)
	{
		this.port = port;
		this.IP = IP;
	}
	
	TCPManager(int port)
	{
		this.port = port;
		try {
			receiveSocket = new ServerSocket(port);
		} catch (Exception e) {}
	}
	
	public void writeMessage(String sendData)
	{
		try {
			socket = new Socket(IP , port);
			DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
			stream.writeBytes(sendData);
			socket.close();
		} catch (IOException e) {}
	}
	
	public String receiveMessage()
	{
		try {
			if(receiveSocket.equals(null))
				receiveSocket = new ServerSocket(port);
			socket = receiveSocket.accept();
			//start of added code
			receiveSocket.setReuseAddress(true);
			receiveSocket.bind(new InetSocketAddress("localhost", port));
			//End of added code
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			return new String(reader.readLine());
		} catch (Exception e) {
			return "";
		}
	}

}
