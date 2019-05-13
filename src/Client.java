import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client {

	private static Socket clientSocket;
	private static BufferedReader reader;
	private static BufferedReader in;
	private static BufferedWriter out;
	public JFrame mainFrame;
	public String address;
	JTextField text;
	JPanel myPanel;
	JLabel test;

	Client() {
		mainFrame = new JFrame();
		myPanel = new JPanel();
		mainFrame.add(myPanel);
		JLabel helloLable = new JLabel("Aдрес!");
		text = new JTextField();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(helloLable);
		myPanel.add(text);
		JButton okButton = new JButton("OK");
		myPanel.add(okButton);
		address = text.getText();
		listenerAdd(okButton);
		test = new JLabel("Answer");
		myPanel.add(test);
	}

	public void listenerAdd(JButton button) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Heyy you");
				
				JPanel mainPanel = new JPanel();
				JLabel helloLable = new JLabel("Ahtung!");
				text = new JTextField();
				mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
				mainPanel.add(helloLable);
				mainPanel.add(text);
				String word = "Blabla";
				int result = JOptionPane.showConfirmDialog(null, mainPanel, "Выберите количество элементов на странице",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					word = text.getText();
				}
				try {
					try {
						clientSocket = new Socket(address, 4004);
						reader = new BufferedReader(new InputStreamReader(System.in));
						in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
						out.write(word + "\n");
						out.flush();
						test.setText(in.readLine());
					} finally {
						System.out.println("Клиент был закрыт...");
						clientSocket.close();
						in.close();
						out.close();
					}
				} catch (IOException n) {
					System.err.println(n);
				}
			}
		};
		button.addActionListener(actionListener);
	}

	public static void main(String[] args) {
		Client client = new Client();
		run(client, 500, 500);

	}

	public static void run(final Client frame, final int wigth, final int hight) {
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