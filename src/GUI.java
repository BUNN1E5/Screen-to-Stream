import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;


public class GUI {

	private JFrame frame;
	private JMenuBar menu;
	private JToggleButton streamButton;
	private JToggleButton watchButton;
	private JTextField ipInput;
	private JTextField portInput;
	private JLabel blankSpace;
	private JLabel blankSpace2;
	private JPanel streamWindow;
	private JLabel picture;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI gui = new GUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		initialize();
		frame.setVisible(true);
	}
	
	public void initialize()
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("Screen To Stream");
		//frame.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		frame.setBackground(new Color(0,0,0,0));
		streamWindow = new JPanel(new GridLayout(1, 1));
		streamWindow.setBounds(100, 100, 450, 300);
		streamWindow.setBackground(new Color(0,0,0,0));
		frame.getContentPane().add(streamWindow);
		cap = new ScreenCap();
		menuInit();
		listenerInit();
	}
	
	public void menuInit()
	{
		menu = new JMenuBar();
		menu.setBounds(0, frame.getHeight() - 33 - menu.getSize().height, frame.getWidth(), 20);
		picture = new JLabel("");
		picture.setHorizontalAlignment(SwingConstants.CENTER);
		blankSpace = new JLabel(" ");
		streamButton = new JToggleButton("stream");
		watchButton = new JToggleButton("watch");
		ipInput = new JTextField();
		portInput = new JTextField();
		blankSpace2 = new JLabel("     ");
		frame.getContentPane().setLayout(null);
		streamWindow.add(picture);
		menu.add(blankSpace);
		menu.add(streamButton);
		menu.add(watchButton);
		menu.add(ipInput);
		menu.add(portInput);
		menu.add(blankSpace2);
		frame.getContentPane().add(menu);
	}
	
	//i am doing stuff
	ScreenCap cap;
	UDPManager udp;
	TCPManager tcp;
	public void streamButtonAction()
	{
		picture.setVisible(false);
		frame.toBack();
		frame.toFront();
		udp = new UDPManager(Integer.parseInt(portInput.getText()));
		tcp = new TCPManager(Integer.parseInt(portInput.getText()));
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				udp.addIP("174.68.74.90");
				udp.addIP("174.65.14.152");
				while(true)
				{
					if(!streamButton.isSelected())
						break;
					udp.sendData(cap.convertToJPEG(streamWindow.getLocationOnScreen(), streamWindow.getSize()));
					frame.setAlwaysOnTop(true);
				}
			}
		}, "Server Thread").start();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					if(!streamButton.isSelected())
						break;
					udp.addIP(tcp.receiveMessage());
				}
			}
		}, "Receiving Thread").start();
	}
	
	public void watchButtonAction()
	{
		udp = new UDPManager(Integer.parseInt(portInput.getText()));
		tcp = new TCPManager(ipInput.getText() , Integer.parseInt(portInput.getText()));
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				picture.setVisible(true);
				try {
					tcp.writeMessage(InetAddress.getLocalHost().getHostAddress());
					tcp.writeMessage(IPManager.getExternalIP());
				} catch (UnknownHostException e) {}
				while(true)
				{
					if(!watchButton.isSelected())
					{
						System.gc();
						picture.setVisible(false);
						frame.toBack();
						frame.toFront();
						break;
					}
					BufferedImage image = cap.displayImage(udp.recieveData(350000));
					frame.setSize(image.getWidth(), image.getHeight() + 33 + menu.getSize().height);
					picture.setIcon(new ImageIcon(image));
				}
			}
		}, "Server Thread").start();
	}
	
	void checkSize()
	{
		if(cap.convertToJPEG(streamWindow.getLocationOnScreen(), streamWindow.getSize()).length > 350000)
		{
			frame.getRootPane().setWindowDecorationStyle(JRootPane.ERROR_DIALOG);
			//frame.getRootPane().setWindowDecorationStyle();
		}
		else if(cap.convertToJPEG(streamWindow.getLocationOnScreen(), streamWindow.getSize()).length < 350000)
		{
			frame.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
		}
	}
	
	public void listenerInit()
	{
		streamButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				streamButtonAction();
			}
		});
		
		watchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				watchButtonAction();
			}
		});
		
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				menu.setBounds(0, frame.getHeight() - 33 - menu.getSize().height, frame.getWidth(), 20);
				streamWindow.setBounds(0, 0, frame.getWidth(), frame.getHeight() - 33 - menu.getSize().height);
				checkSize();
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				menu.setBounds(0, frame.getHeight() - 33 - menu.getSize().height, frame.getWidth(), 20);
				streamWindow.setBounds(0, 0, frame.getWidth(), frame.getHeight() - 33 - menu.getSize().height);
				checkSize();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				checkSize();
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		
		portInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < '0' || e.getKeyChar() > '9' || e.getKeyChar() == KeyEvent.VK_BACK_SPACE)
				{
					e.consume();
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
	}

}
