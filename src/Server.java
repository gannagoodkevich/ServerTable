import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server {

	private static Socket clientSocket;
	private static ServerSocket server;
	private static BufferedReader in;
	private static BufferedWriter out;
	public JFrame mainFrame;
	JPanel mainPanel;
	boolean flag = true; 
	JLabel helloLable;

	Server() {
		mainFrame = new JFrame();

	}

	public static void main(String[] args) throws UnknownHostException {
		Server mainserver = new Server();
		run(mainserver, 500, 500);
		InetAddress adresse = InetAddress.getLocalHost();
		mainserver.mainPanel = new JPanel();
		mainserver.mainFrame.add(mainserver.mainPanel);
		mainserver.helloLable = new JLabel("Сервер запущен!");
		JLabel adddressLable = new JLabel("Вот адрес: " + adresse.getHostAddress());
		mainserver.mainPanel.setLayout(new BoxLayout(mainserver.mainPanel, BoxLayout.Y_AXIS));
		mainserver.mainPanel.add(mainserver.helloLable);
		mainserver.mainPanel.add(adddressLable);
		JLabel test = new JLabel("Answer");
		JButton endButton = new JButton("end");
		mainserver.mainPanel.add(endButton);
		mainserver.listenerAdd(endButton, mainserver);
		mainserver.mainPanel.add(test);
		try {
			try {
				server = new ServerSocket(4004);
				while (mainserver.flag) {
					clientSocket = server.accept();
					try {
						in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
						out.write("Вот адрес: ");
						out.write(adresse.getHostName() + " " + adresse.getHostAddress() + "\n");
						test.setText(in.readLine());
						System.out.println(mainserver.flag);
						out.flush();
					} finally {
						System.out.println("До свидания");
						clientSocket.close();
						in.close();
						out.close();
					}
				}
			} finally {
				System.out.println("Сервер закрыт!");
				server.close();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public void listenerAdd(JButton button, Server mainserver) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainserver.helloLable.setText("Server end!");
				System.out.println("Server End");
				mainserver.flag = false;
			}
		};
		button.addActionListener(actionListener);
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
}