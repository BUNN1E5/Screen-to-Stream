import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;


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
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		frame.setBackground(new Color(0,0,0,0));
		streamWindow = new JPanel(new GridLayout(1, 1));
		streamWindow.setBounds(100, 100, 450, 300);
		streamWindow.setBackground(new Color(0,0,0,0));
		frame.add(streamWindow);
		menuInit();
		listenerInit();
	}
	
	public void menuInit()
	{
		menu = new JMenuBar();
		menu.setBounds(0, frame.getHeight() - 33 - menu.getSize().height, frame.getWidth(), 20);
		picture = new JLabel("");
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
	
	ScreenCap cap;
	UDPManager udp;
	TCPManager tcpManager;
	public void streamButtonAction()
	{
		cap = new ScreenCap();
		udp = new UDPManager(5460);
		udp.addIP("192.168.0.101");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					if(!streamButton.isSelected())
						break;
					if(udp.sendData(cap.getSendData(streamWindow.getLocationOnScreen(), streamWindow.getSize())))
						System.out.println("sentData");
					frame.toFront();
				}
			}
		}, "Server Thread").start();
	}
	
	public void watchButtonAction()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					if(!watchButton.isSelected())
					{
						System.gc();
						break;
					}
					cap = new ScreenCap();
					udp = new UDPManager(5460);
					udp.addIP("192.168.0.101");
					picture.setIcon(new ImageIcon(udp.recieveData(214748346)));
					frame.toFront();
					//picture.setIcon(new ImageIcon(cap.screenCap(streamWindow.getLocationOnScreen(), streamWindow.getSize())));
					//picture.setIcon(new ImageIcon(cap.getSendData(streamWindow.getLocationOnScreen(), streamWindow.getSize())));
				}
			}
		}, "Server Thread").start();
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
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				menu.setBounds(0, frame.getHeight() - 33 - menu.getSize().height, frame.getWidth(), 20);
				streamWindow.setBounds(0, 0, frame.getWidth(), frame.getHeight() - 33 - menu.getSize().height);
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				
			}
		});
	}

}
