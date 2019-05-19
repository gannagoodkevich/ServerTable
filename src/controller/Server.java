package controller;

// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.net.*;

// Server class 
public class Server {
	JFrame mainFrame;
	ServerSocket serverSocket;
	ClientHandler tread;
	
	Server() throws UnknownHostException {
		mainFrame = new JFrame();
		InetAddress adresse = InetAddress.getLocalHost();
		JPanel mainPanel = new JPanel();
		mainFrame.add(mainPanel);
		JLabel adddressLable = new JLabel("Адрес сервера: " + adresse.getHostAddress());
		Font adressFont = adddressLable.getFont();
		int size = adressFont.getSize() * 2;
		adddressLable.setFont(new Font(adressFont.getName(), Font.ITALIC, size));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		JPanel lablePanel = new JPanel();
		//lablePanel.setBackground(Color.CYAN);
		mainPanel.add(lablePanel);
		lablePanel.add(adddressLable);
		JButton beginButtin = new JButton("Start working with server");
		JButton endButton = new JButton("End working with server");
		JPanel buttonPanel = new JPanel();
		mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
		mainPanel.add(buttonPanel);
		//buttonPanel.setBackground(Color.CYAN);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		//buttonPanel.add(Box.createRigidArea(new Dimension(40, 0)));
		buttonPanel.add(beginButtin);
		buttonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
		buttonPanel.add(endButton);
		listenerBegin(beginButtin, this);
		listenerEnd(endButton, this);
	}

	public static void main(String[] args) throws IOException {
		Server serv = new Server();
		// server is listening on port 5056
		run(serv, 600, 300);
		serv.serverSocket = new ServerSocket(5056);
		while (true) {
			Socket socket = null;

			try {
				// socket object to receive incoming client requests
				socket = serv.serverSocket.accept();

				System.out.println("A new client is connected : " + socket);

				// obtaining input and out streams
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				System.out.println("Assigning new thread for this client");

				// create a new thread object
				serv.tread = new ClientHandler(socket, dis, dos);

				// Invoking the start() method
				//serv.tread.start();

			} catch (Exception ex) {
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ex.printStackTrace();
			}
		}
		// running infinite loop for getting
		// client request
		
	}

	public static void run(final Server frame, final int wigth, final int hight) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.mainFrame.setTitle(frame.getClass().getSimpleName());
				frame.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.mainFrame.setSize(wigth, hight);
				frame.mainFrame.setVisible(true);
			}
		});
	}
	
	public void listenerBegin(JButton button, Server serv) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Started server");
				serv.tread.start();
				serv.tread.exit = false;
			}
		};
		button.addActionListener(actionListener);
	}
	
	public void listenerEnd(JButton button, Server serv) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Ended server");
				serv.tread.exit = true;
			}
		};
		button.addActionListener(actionListener);
	}
	
}

// ClientHandler class 
class ClientHandler extends Thread {
	Boolean exit = false;
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run() {
		String received;
		String toreturn;
		while (true) {
			try {

				// Ask user what he wants
				dos.writeUTF("What do you want?[Date | Time]..\n" + "Type Exit to terminate connection.");

				// receive the answer from client
				received = dis.readUTF();
				
				
				
				if (exit) {
					System.out.println("Client " + this.s + " sends exit...");
					System.out.println("Closing this connection.");
					this.s.close();
					System.out.println("Connection closed");
					break;
				}

				// creating Date object
				Date date = new Date();

				// write on output stream based on the
				// answer from the client
				switch (received) {

				case "Date":
					toreturn = fordate.format(date);
					dos.writeUTF(toreturn);
					break;

				case "Time":
					toreturn = fortime.format(date);
					dos.writeUTF(toreturn);
					break;

				default:
					dos.writeUTF("Invalid input");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			// closing resources
			this.dis.close();
			this.dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}