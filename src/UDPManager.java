import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class UDPManager {

	ArrayList<InetAddress> adresses = new ArrayList<InetAddress>();
	DatagramSocket socket;
	int port;
	
	public UDPManager(int port)
	{
		this.port = port;
	}
	
	public void addIP(String IP)
	{
		try {
			adresses.add(InetAddress.getByName(IP));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void removeIP(String IP)
	{
		adresses.remove(adresses.indexOf(IP));
	}
	

	public boolean sendData(byte[] data)
	{
		try { 
			socket = new DatagramSocket();
			for(InetAddress adress : adresses)
			{
				DatagramPacket packet = new DatagramPacket(data, data.length, adress, port);
				socket.send(packet);
			}
			return true;
		} catch (Exception e) {
			return false;
		} 
	}
	
	public byte[] recieveData(int bufferSize)
	{
		byte[] data = new byte[bufferSize];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		packet.setPort(port);
		try {
			socket = new DatagramSocket(port);
			socket.receive(packet);
			socket.close();
			return packet.getData();
		} catch (Exception e) {
			return new byte[]{};
		}
		
	}
}
