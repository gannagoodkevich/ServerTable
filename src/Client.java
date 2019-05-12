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

    private static Socket clientSocket; //сокет для общения
    private static BufferedReader reader; // нам нужен ридер читающий с консоли, иначе как
    // мы узнаем что хочет сказать клиент?
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет
    public JFrame mainFrame;
    public String address;
    JTextField text;
    JPanel myPanel;
    JLabel test;
    
    
    Client(){
    	mainFrame = new JFrame();
    	myPanel = new JPanel();
		mainFrame.add(myPanel);
		JLabel helloLable = new JLabel("Aдрес!");
		text= new JTextField();
		myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
		myPanel.add(helloLable);
		myPanel.add(text);
		JButton okButton = new JButton("OK");
		myPanel.add(okButton);
		listenerAdd(okButton);
		test = new JLabel("Answer");
        myPanel.add(test);
    }
    
    public void listenerAdd(JButton button) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Heyy you");
				address = text.getText();
				//System.out.println(address);
				JPanel mainPanel = new JPanel();
				JLabel helloLable = new JLabel("Ahtung!");
				text= new JTextField();
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
		                // адрес - локальный хост, порт - 4004, такой же как у сервера
		                //clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
		            	//o can initadress do!!!
		                clientSocket = new Socket(address , 4004);
		                //  у сервера доступ на соединение
		                reader = new BufferedReader(new InputStreamReader(System.in));
		                // читать соообщения с сервера
		                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		                // писать туда же
		                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		                
		               // System.out.println("Вы что-то хотели сказать? Введите это здесь:");
		                // если соединение произошло и потоки успешно созданы - мы можем
		                //  работать дальше и предложить клиенту что то ввести
		                // если нет - вылетит исключение
		                out.write(word + "\n");
		                
		                // не напишет в консоль
		              //  out.write(word + "\n"); // отправляем сообщение на сервер
		                out.flush();
		                //String serverWord = in.readLine(); // ждём, что скажет сервер
		               test.setText(in.readLine());
		               // System.out.println(serverWord); // получив - выводим на экран
		            } finally { // в любом случае необходимо закрыть сокет и потоки
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
		// mytable.updateTable(uni);
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