import java.io.*;
import java.net.*;


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
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	public String receiveMessage()
	{
		try {
			if(receiveSocket.equals(null))
				receiveSocket = new ServerSocket(port);
			socket = receiveSocket.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println(reader.readLine());
			return new String(reader.readLine());
		} catch (Exception e) {
			return "";
		}
	}

}
