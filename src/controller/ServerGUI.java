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

/*
 * The server as a GUI
 */
public class ServerGUI{
	
	private static final long serialVersionUID = 1L;
	// the stop and start buttons
	private JFrame jFrame;
	private JButton startButton;
	private JButton stopButton;
	// JTextArea for the chat room and the events
	private JTextArea chat, event;
	// The port number
	private JTextField tPortNumber;
	// my server
	
	private Server server;
	
	
	// server constructor that receive the port to listen to for connection as parameter
	ServerGUI(int port) {
		jFrame = new JFrame("Chat Server");
		server = null;
		// in the NorthPanel the PortNumber the Start and Stop buttons
		JPanel north = new JPanel();
		north.add(new JLabel("Port number: "));
		tPortNumber = new JTextField("  " + port);
		north.add(tPortNumber);
		// to stop or start the server, we start with "Start"
		startButton = new JButton("Start");
		//stopStart.addActionListener(this);
		beginServer(startButton, this);
		stopButton = new JButton("Stop");
		endServer(stopButton, this);
		north.add(startButton);
		north.add(stopButton);
		jFrame.add(north, BorderLayout.NORTH);
		
		// the event and chat room
		JPanel center = new JPanel(new GridLayout(2,1));
		jFrame.add(center);
		chat = new JTextArea(80,80);
		chat.setEditable(false);
		appendRoom("Chat room.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(80,80);
		event.setEditable(false);
		appendEvent("Events log.\n");
		center.add(new JScrollPane(event));	
		jFrame.add(center);
		
		// need to be informed when the user click the close button on the frame
		//jFrame.addWindowListener(this);
		jFrame.setSize(400, 600);
		jFrame.setVisible(true);
	}		

	// append message to the two JTextArea
	// position at the end
	void appendRoom(String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);
	}
	void appendEvent(String str) {
		event.append(str);
		event.setCaretPosition(chat.getText().length() - 1);
	}
	
	public void beginServer(JButton button, ServerGUI sg) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port;
				try {
					port = Integer.parseInt(tPortNumber.getText().trim());
				}
				catch(Exception er) {
					appendEvent("Invalid port number");
					return;
				}
				// ceate a new Server
				server = new Server(port, sg);
				// and start it as a thread
				new ServerRunning().start();
				//stopStart.setText("Stop");
				tPortNumber.setEditable(false);
			}
		};
		button.addActionListener(actionListener);

	}
	
	public void endServer(JButton button, ServerGUI sg) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(server != null) {
					server.stop();
					server = null;
					tPortNumber.setEditable(true);
					//stopStart.setText("Start");
					return;
				}
			}
		};
		button.addActionListener(actionListener);

	}
	
	// start or stop where clicked
	public void actionPerformed(ActionEvent e) {
		// if running we have to stop
		/*if(server != null) {
			server.stop();
			server = null;
			tPortNumber.setEditable(true);
			stopStart.setText("Start");
			return;
		}*/
      	// OK start the server	
		int port;
		try {
			port = Integer.parseInt(tPortNumber.getText().trim());
		}
		catch(Exception er) {
			appendEvent("Invalid port number");
			return;
		}
		// ceate a new Server
		server = new Server(port, this);
		// and start it as a thread
		new ServerRunning().start();
		//stopStart.setText("Stop");
		tPortNumber.setEditable(false);
	}
	
	// entry point to start the Server
	public static void main(String[] arg) {
		// start server default port 1500
		new ServerGUI(1500);
	}

	/*
	 * If the user click the X button to close the application
	 * I need to close the connection with the server to free the port
	 */
	/*public void windowClosing(WindowEvent e) {
		// if my Server exist
		if(server != null) {
			try {
				server.stop();			// ask the server to close the conection
			}
			catch(Exception eClose) {
			}
			server = null;
		}
		// dispose the frame
		dispose();
		System.exit(0);
	}
	// I can ignore the other WindowListener method
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	/*
	 * A thread to run the Server
	 */
	class ServerRunning extends Thread {
		public void run() {
			server.start();         // should execute until if fails
			// the server failed
			//stopStart.setText("Start");
			tPortNumber.setEditable(true);
			appendEvent("Server crashed\n");
			server = null;
		}
	}

}
