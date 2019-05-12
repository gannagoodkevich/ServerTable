import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InterraptedSocket {
	public static void main(String[] arg) {
		EventQueue.invokeLater(() -> {
			JFrame frame = new InterruptibleSocketFrame();
			frame.setTitle("InterraptedSocket");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}

class InterruptibleSocketFrame extends JFrame {
	private Scanner in;
	private JButton interruptBitton;
	private JButton blockButton;
	private JButton cancelButton;
	private JTextArea massege;
	private TestServer server;
	private Thread connectThread;

	public InterruptibleSocketFrame() {
		JPanel northPanel = new JPanel();
		add(northPanel, BorderLayout.NORTH);

		final int TEXT_ROWS = 20;
		final int TEXT_COLUMNS = 60;
		massege = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
		add(new JScrollPane(massege));

		interruptBitton = new JButton("Interrupt");
		blockButton = new JButton("Blocking");

		northPanel.add(interruptBitton);
		northPanel.add(blockButton);

		interruptBitton.addActionListener(event -> {
			interruptBitton.setEnabled(false);
			blockButton.setEnabled(false);
			cancelButton.setEnabled(true);
			connectThread = new Thread(() -> {
				try {
					connectInterruptibly();
				} catch (IOException e) {
					massege.append("\nInterruptedSocket: interrrupt " + e);
				}
			});
			connectThread.start();
		});
		blockButton.addActionListener(event -> {
			interruptBitton.setEnabled(false);
			blockButton.setEnabled(false);
			cancelButton.setEnabled(true);
			connectThread = new Thread(() -> {
				try {
					connectBlocking();
				} catch (IOException e) {
					massege.append("\nInterruptedSocket: block" + e);
				}
			});
			connectThread.start();
		});
		cancelButton = new JButton("Cancel");
		cancelButton.setEnabled(false);
		northPanel.add(cancelButton);
		cancelButton.addActionListener(event -> {
			connectThread.interrupt();
			cancelButton.setEnabled(false);
		});
		server = new TestServer();
		new Thread(server).start();
		pack();
	}

	public void connectInterruptibly() throws IOException {
		massege.append("Interrupt: \n");
		try (SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 8189))) {
			in = new Scanner(channel, "UTF-8");
			while (!Thread.currentThread().isInterrupted()) {
				massege.append("Reading");
				if (in.hasNextLine()) {
					String line = in.nextLine();
					massege.append(line);
					massege.append("\n");
				}
			}
		} finally {
			EventQueue.invokeLater(() -> {
				massege.append("Channel closed\n");
				interruptBitton.setEnabled(true);
				blockButton.setEnabled(true);
			});
		}
	}

	public void connectBlocking() throws IOException {
		massege.append("Blocking:\n");
		try (Socket socket = new Socket("localhost", 8189)) {
			in = new Scanner(socket.getInputStream(), "UTF-8");
			while (!Thread.currentThread().isInterrupted()) {
				massege.append("Reading");
				if (in.hasNextLine()) {
					String line = in.nextLine();
					massege.append(line);
					massege.append("\n");
				}
			}
		} finally {
			EventQueue.invokeLater(() -> {
				massege.append("Socket closed\n");
				interruptBitton.setEnabled(true);
				blockButton.setEnabled(true);
			});
		}
	}

	class TestServer implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try (ServerSocket s = new ServerSocket(8189)) {
				while (true) {
					Socket incoming = s.accept();
					Runnable r = new TestServerHandler(incoming);
					Thread t = new Thread(r);
					t.start();
				}
			} catch (IOException e) {
				massege.append("\nTestServer.run: " + e);
			}
		}

	}

	class TestServerHandler implements Runnable {
		private Socket incoming;
		private int counter;

		public TestServerHandler(Socket i) {
			incoming = i;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				try {
					OutputStream outStream = incoming.getOutputStream();
					PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, "UTF-8"), true);
					while (counter < 100) {
						counter++;
						if (counter <= 10) {
							out.print(counter);
						}
						Thread.sleep(100);
					}
				} 
				finally {
					incoming.close();
					massege.append("Closing server\n");
				}
			} catch (Exception e) {
				massege.append("\nTestServerHendler.run: " + e);
			}
		}

	}
}