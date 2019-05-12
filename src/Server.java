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

	private static Socket clientSocket; // сокет дл€ общени€
	private static ServerSocket server; // серверсокет
	private static BufferedReader in; // поток чтени€ из сокета
	private static BufferedWriter out; // поток записи в сокет	
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
		JLabel helloLable = new JLabel("—ервер запущен!");
		JLabel adddressLable  = new JLabel("¬от адрес: " + adresse.getHostAddress());
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(helloLable);
		mainPanel.add(adddressLable);
		JLabel test = new JLabel("Answer");
		// не долго дума€ отвечает клиенту
		mainPanel.add(test);
		try {
			try {
				server = new ServerSocket(4004); // серверсокет прослушивает порт 4004
				 // хорошо бы серверу
				// объ€вить о своем запуске
				clientSocket = server.accept(); // accept() будет ждать пока
				// кто-нибудь не захочет подключитьс€
				try { // установив св€зь и воссоздав сокет дл€ общени€ с клиентом можно перейти
						// к созданию потоков ввода/вывода.
						// теперь мы можем принимать сообщени€
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					// и отправл€ть
					out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
					out.write("¬от адрес: ");
					out.write(adresse.getHostName() + " " + adresse.getHostAddress() + "\n");
					//String word = in.readLine(); // ждЄм пока клиент что-нибудь нам напишет
					//System.out.println(word);
					test.setText(in.readLine());
					out.write("ѕривет, это —ервер!" + "\n");
					out.flush(); // выталкиваем все из буфера

				} finally { // в любом случае сокет будет закрыт
					System.out.println("ƒо свидани€");
					clientSocket.close();
					// потоки тоже хорошо бы закрыть
					in.close();
					out.close();
				}
			} finally {
				System.out.println("—ервер закрыт!");
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