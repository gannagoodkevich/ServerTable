import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Server {

	private static Socket clientSocket; // ����� ��� �������
	private static ServerSocket server; // �����������
	private static BufferedReader in; // ����� ������ �� ������
	private static BufferedWriter out; // ����� ������ � �����	
	public JFrame mainFrame;
	
	Server(){
		mainFrame = new JFrame();
		
	}
	
	public static void main(String[] args) throws UnknownHostException {
		Server mainserver = new Server();
		run(mainserver, 500, 500);
		InetAddress adresse = InetAddress.getLocalHost();
		JPanel mainPanel = new JPanel();
		mainserver.mainFrame.add(mainPanel);
		JLabel helloLable = new JLabel("������ �������!");
		JLabel adddressLable  = new JLabel("��� �����: " + adresse.getHostAddress());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(helloLable);
		mainPanel.add(adddressLable);
		JLabel test = new JLabel("Answer");
		// �� ����� ����� �������� �������
		mainPanel.add(test);
		try {
			try {
				server = new ServerSocket(4004); // ����������� ������������ ���� 4004
				 // ������ �� �������
				// �������� � ����� �������
				clientSocket = server.accept(); // accept() ����� ����� ����
				// ���-������ �� ������� ������������
				try { // ��������� ����� � ��������� ����� ��� ������� � �������� ����� �������
						// � �������� ������� �����/������.
						// ������ �� ����� ��������� ���������
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					// � ����������
					out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
					out.write("��� �����: ");
					out.write(adresse.getHostName() + " " + adresse.getHostAddress() + "\n");
					//String word = in.readLine(); // ��� ���� ������ ���-������ ��� �������
					//System.out.println(word);
					test.setText(in.readLine());
					out.write("������, ��� ������!" + "\n");
					out.flush(); // ����������� ��� �� ������

				} finally { // � ����� ������ ����� ����� ������
					System.out.println("�� ��������");
					clientSocket.close();
					// ������ ���� ������ �� �������
					in.close();
					out.close();
				}
			} finally {
				System.out.println("������ ������!");
				server.close();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
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