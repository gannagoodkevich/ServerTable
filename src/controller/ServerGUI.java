package controller;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * The server as a GUI
 */
public class ServerGUI {

	private static final long serialVersionUID = 1L;
	private JFrame jFrame;
	private JButton startButton;
	private JButton stopButton;
	private JTextArea event;
	private JTextField tPortNumber;
	private Server server;
	private JLabel serverAdress;

	ServerGUI(int port) {
		jFrame = new JFrame("Chat Server");
		server = null;
		JPanel north = new JPanel();
		north.add(new JLabel("Port number: "));
		tPortNumber = new JTextField("  " + port);
		north.add(tPortNumber);
		startButton = new JButton("Start");
		beginServer(startButton, this);
		stopButton = new JButton("Stop");
		endServer(stopButton, this);
		north.add(startButton);
		north.add(stopButton);
		jFrame.add(north, BorderLayout.NORTH);
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.add(new JLabel("Server adress"));
		serverAdress = new JLabel();
		center.add(serverAdress);
		event = new JTextArea(80, 80);
		event.setEditable(false);
		center.add(new JScrollPane(event));
		jFrame.add(center);
		jFrame.setSize(400, 600);
		jFrame.setVisible(true);
	}

	void appendEvent(String str) {
		event.append(str);
		//event.setCaretPosition(chat.getText().length() - 1);
	}

	public void beginServer(JButton button, ServerGUI sg) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port;
				try {
					port = Integer.parseInt(tPortNumber.getText().trim());
				} catch (Exception er) {
					appendEvent("Invalid port number");
					return;
				}
				server = new Server(port, sg);
				try {
					InetAddress adresse = InetAddress.getLocalHost();
					serverAdress.setText(adresse.getHostAddress());
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new ServerRunning().start();
				tPortNumber.setEditable(false);
			}
		};
		button.addActionListener(actionListener);

	}

	public void endServer(JButton button, ServerGUI sg) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (server != null) {
					server.stop();
					server = null;
					tPortNumber.setEditable(true);
					return;
				}
			}
		};
		button.addActionListener(actionListener);

	}

	public void actionPerformed(ActionEvent e) {
		/*
		 * if(server != null) { server.stop(); server = null;
		 * tPortNumber.setEditable(true); stopStart.setText("Start"); return; }
		 */
		int port;
		try {
			port = Integer.parseInt(tPortNumber.getText().trim());
		} catch (Exception er) {
			appendEvent("Invalid port number");
			return;
		}
		server = new Server(port, this);
		new ServerRunning().start();
		tPortNumber.setEditable(false);
	}

	public static void main(String[] arg) {
		new ServerGUI(1500);
	}

	/*
	 * If the user click the X button to close the application I need to close the
	 * connection with the server to free the port
	 */
	/*
	 * public void windowClosing(WindowEvent e) { // if my Server exist if(server !=
	 * null) { try { server.stop(); // ask the server to close the conection }
	 * catch(Exception eClose) { } server = null; } // dispose the frame dispose();
	 * System.exit(0); } // I can ignore the other WindowListener method public void
	 * windowClosed(WindowEvent e) {} public void windowOpened(WindowEvent e) {}
	 * public void windowIconified(WindowEvent e) {} public void
	 * windowDeiconified(WindowEvent e) {} public void windowActivated(WindowEvent
	 * e) {} public void windowDeactivated(WindowEvent e) {}
	 * 
	 * /* A thread to run the Server
	 */
	class ServerRunning extends Thread {
		public void run() {
			server.start(); // should execute until if fails
			// the server failed
			// stopStart.setText("Start");
			tPortNumber.setEditable(true);
			appendEvent("Server crashed\n");
			server = null;
		}
	}

}
