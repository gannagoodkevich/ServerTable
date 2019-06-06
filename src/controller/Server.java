package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import model.Department;
import model.Faculty;
import model.Lecturer;
import model.Uni;

/*
007
 * The server that can be run both as a console application or a GUI
008
 */
public class Server {
	// a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread> al;
	// if I am in a GUI
	private ServerGUI sg;
	// to display time
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;

	String fileName;
	private Uni currentUniversity;
	public int numOfRows = 10;
	int numOfRowsEnd = numOfRows;
	int numOfRowsStart = 0;
	int currPage = 1;

	/*
	 * 025 server constructor that receive the port to listen to for connection as
	 * parameter 026 in console 027
	 */
	public Server(int port) {
		this(port, null);
	}

	public Server(int port, ServerGUI sg) {
		// GUI or not
		this.sg = sg;
		// the port
		this.port = port;
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		// ArrayList for the Client list
		al = new ArrayList<ClientThread>();
	}

	public void start() {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try {
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);
			// infinite loop to wait for connections
			while (keepGoing) {
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");
				Socket socket = serverSocket.accept(); // accept connection
				// if I was asked to stop
				if (!keepGoing)
					break;
				ClientThread t = new ClientThread(socket); // make a thread of it
				al.add(t); // save it in the ArrayList
				t.start();
			}
			// I was asked to stop
			try {
				serverSocket.close();
				for (int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					} catch (IOException ioE) {
						// not much I can do
					}
				}
			} catch (Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
			String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}

	/*
	 * 091 For the GUI to stop the server 092
	 */
	protected void stop() {
		keepGoing = false;
		// connect to myself as Client to exit statement
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		} catch (Exception e) {
			// nothing I can really do
		}
	}

	/*
	 * 105 Display an event (not a message) to the console or the GUI 106
	 */
	private void display(String msg) {

		if (sg == null)
			System.out.println(msg + "\n");
		else
			sg.appendEvent(msg + "\n");
	}

	/*
	 * 115 to broadcast a message to all Clients 116
	 */
	/*
	 * private synchronized void broadcast(String message) { // add HH:mm:ss and \n
	 * to the message // String time = sdf.format(new Date()); String messageLf =
	 * message + "\n"; // display message on console or GUI if (sg == null)
	 * System.out.print(messageLf); else sg.appendRoom(messageLf); // append in the
	 * room window
	 * 
	 * // we loop in reverse order in case we would have to remove a Client //
	 * because it has disconnected for (int i = al.size(); --i >= 0;) { ClientThread
	 * ct = al.get(i); // try to write to the Client if it fails remove it from the
	 * list if (!ct.writeMsg(messageLf)) { al.remove(i);
	 * display("Disconnected Client " + ct.username + " removed from list."); } } }
	 */

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for (int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			// found it
			if (ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	/*
	 * 153 To run as a console application just open a console window and: 154 >
	 * java Server 155 > java Server portNumber 156 If the port number is not
	 * specified 1500 is used 157
	 */
	/*
	 * public static void main(String[] args) { // start server on port 1500 unless
	 * a PortNumber is specified int portNumber = 1500; switch(args.length) { case
	 * 1: try { portNumber = Integer.parseInt(args[0]); } catch(Exception e) {
	 * System.out.println("Invalid port number.");
	 * System.out.println("Usage is: > java Server [portNumber]"); return; } case 0:
	 * break; default: System.out.println("Usage is: > java Server [portNumber]");
	 * return;
	 * 
	 * } // create a server object and start it Server server = new
	 * Server(portNumber); server.start(); }
	 * 
	 * /** One instance of this thread will run for each client
	 */
	class ClientThread extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		// my unique id (easier for deconnection)
		int id;
		// the Username of the Client
		String username;
		// the only type of message a will receive
		ChatMessage cm;
		// the date I connect

		// String date;

		// Constructore

		ClientThread(Socket socket) {
			// a unique id
			id = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try {
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput = new ObjectInputStream(socket.getInputStream());
				// read the username
				username = (String) sInput.readObject();
				display(username + "in progress.");
			} catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			// have to catch ClassNotFoundException
			// but I read a String, I am sure it will work
			catch (ClassNotFoundException e) {
			}
			// date = new Date().toString() + "\n";
		}

		// what will run forever
		public void run() {
			// to loop until LOGOUT
			boolean keepGoing = true;
			while (keepGoing) {
				// read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				} catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;
				} catch (ClassNotFoundException e2) {
					break;
				}
				// the messaage part of the ChatMessage

				// Switch on the type of message receive
				switch (cm.getType()) {
				case ChatMessage.FILE_NEW:
					List<String> lecturer = cm.getLecturer();
					createNewFile(cm.getFirstMessage(), lecturer);
					fileName = cm.getFirstMessage();
					// broadcast(username + ": " + message);
					// writeMsg("Server is answering!!!");
					break;
				case ChatMessage.FILE_OPEN:
					// String message = cm.getMessage();
					System.out.println("FILE_OPEN in server switching");
					fileName = cm.getFirstMessage();
					String name = "C:\\Users\\Андрей\\git\\Servertable\\Servertable\\" + fileName + ".xml";
					fileName = name;
					File newFile = new File(name);
					openFile(name);
					// broadcast(username + ": " + message);

					break;
				case ChatMessage.ADD_LECT:
					// String message = cm.getMessage();
					System.out.println("LECT_SERVER in server switching");
					System.out.println(cm.getUni().size());
					for (int i = 0; i < cm.getUni().size(); i++) {
						System.out.println("trym" + cm.getUni().get(i)[0]);
					}
					addLect(cm.getUni());
					// broadcast(username + ": " + message);

					break;

				case ChatMessage.TURN_LEFT:
					// String message = cm.getMessage();
					System.out.println("TURN_LEFT in server switching");
					// System.out.println(cm.getUni().size());
					turnLeft();
					// broadcast(username + ": " + message);

					break;
				case ChatMessage.TURN_RIGHT:
					// String message = cm.getMessage();
					System.out.println("TURN_RIGHT in server switching");
					// System.out.println(cm.getUni().size());
					turnRight();
					// broadcast(username + ": " + message);

					break;
				case ChatMessage.GO_TO_HEAD:
					// String message = cm.getMessage();
					System.out.println("GO_TO_HEAD in server switching");
					// System.out.println(cm.getUni().size());
					goToHead();
					// broadcast(username + ": " + message);

					break;
				case ChatMessage.GO_TO_TAIL:
					// String message = cm.getMessage();
					System.out.println("GO_TO_TAIL in server switching");
					// System.out.println(cm.getUni().size());
					goToTail();
					// broadcast(username + ": " + message);

					break;
				case ChatMessage.CLOSE_SEARCH:
					// String message = cm.getMessage();
					System.out.println("TURN_LEFT in server switching");
					// System.out.println(cm.getUni().size());
					currentUniversity = new Uni("New Uni");
					SAXExample sax;
					try {
						sax = new SAXExample(fileName);
						currentUniversity = sax.uni;
					} catch (ParserConfigurationException | SAXException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// there
					System.out.println(currentUniversity.getFaculty(1).getTitle() + " open");
					UniversityController unicontr = new UniversityController();
					
					List<String[]> uniList = unicontr.getUniversity(currentUniversity);
					writeMsg(uniList);
					// broadcast(username + ": " + message);

					break;
				case ChatMessage.SEARCH_FAC:
					//display(username + "disconnected with a LOGOUT message.");
					List<String[]> answer = new ArrayList<String[]>();
					String[] str = {"SEARCH"};
					answer.add(str);
					//writeMsg(answer);
					System.out.println("search by faculty " + cm.getFirstMessage() + " " + cm.getSecondMessage());
					List<String[]> answer1 = findByFac(cm.getFirstMessage(), cm.getSecondMessage());
					for(int i=0; i<answer1.size(); i++) {
						answer.add(answer1.get(i));
					}
					currentUniversity = new Uni("New Uni");
					for (String[] lect : answer1) {
						Faculty fac = new Faculty(lect[0]);
						System.out.println(lect.length);
						Department dep = new Department(lect[1]);
						
						Lecturer lectuer = new Lecturer(lect[2], lect[3], lect[4], lect[5]);

						currentUniversity.addFaculty(fac);
						fac.addDepartment(dep);
						dep.addLecturer(lectuer);
					}
					System.out.println(currentUniversity.getFaculty(0).getTitle());
					//System.out.println(answer.get(1)[3]);
					writeMsg(answer);
					
					
					//System.out.println("помогити " + answer1.get(1)[1]);
					break;
				case ChatMessage.SEARCH_NAME:
					System.out.println("search by name");
					List<String[]> answerName = new ArrayList<String[]>();
					String[] strName = {"SEARCH"};
					answerName.add(strName);
					//writeMsg(answer);
					System.out.println("search by name " + cm.getFirstMessage() + " " + cm.getSecondMessage());
					List<String[]> answer1Name = findByName(cm.getFirstMessage(), cm.getSecondMessage());
					for(int i=0; i<answer1Name.size(); i++) {
						answerName.add(answer1Name.get(i));
					}
					currentUniversity = new Uni("New Uni");
					for (String[] lect : answer1Name) {
						Faculty fac = new Faculty(lect[0]);
						System.out.println(lect.length);
						Department dep = new Department(lect[1]);
						
						Lecturer lectuer = new Lecturer(lect[2], lect[3], lect[4], lect[5]);

						currentUniversity.addFaculty(fac);
						fac.addDepartment(dep);
						dep.addLecturer(lectuer);
					}
					System.out.println(currentUniversity.getFaculty(0).getTitle());
					//System.out.println(answer.get(1)[3]);
					writeMsg(answerName);
					break;
				case ChatMessage.SEARCH_YEAR:
					System.out.println("search by year");
					List<String[]> answerYear = new ArrayList<String[]>();
					String[] strYear = {"SEARCH"};
					answerYear.add(strYear);
					//writeMsg(answer);
					System.out.println("search by name " + cm.getFirstMessage() + " " + cm.getSecondMessage());
					List<String[]> answer1Year = findByYear(cm.getFirstMessage(), cm.getSecondMessage());
					for(int i=0; i<answer1Year.size(); i++) {
						answerYear.add(answer1Year.get(i));
					}
					
					currentUniversity = new Uni("New Uni");
					for (String[] lect : answer1Year) {
						Faculty fac = new Faculty(lect[0]);
						System.out.println(lect.length);
						Department dep = new Department(lect[1]);
						
						Lecturer lectuer = new Lecturer(lect[2], lect[3], lect[4], lect[5]);

						currentUniversity.addFaculty(fac);
						fac.addDepartment(dep);
						dep.addLecturer(lectuer);
					}
					System.out.println(currentUniversity.getFaculty(0).getTitle());
					//System.out.println(answer.get(1)[3]);
					writeMsg(answerYear);
					break;
				case ChatMessage.DELETE_FAC:
					System.out.println("delete by faculty");
					List<String[]> answerFacD = new ArrayList<String[]>();
					//writeMsg(answer);
					System.out.println("delete by fac" + cm.getFirstMessage() + " " + cm.getSecondMessage());
					Integer answer1FacD = deleteByFac(cm.getFirstMessage(), cm.getSecondMessage());
					String[] strr = {"DELETE", answer1FacD.toString()};
					System.out.println(strr[1]);
					answerFacD.add(strr);
					//System.out.println(answer.get(1)[3]);
					writeMsg(answerFacD);
					break;
				case ChatMessage.DELETE_NAME:
					System.out.println("delete by name");
					List<String[]> answerNameD = new ArrayList<String[]>();
					//writeMsg(answer);
					System.out.println("search by name " + cm.getFirstMessage() + " " + cm.getSecondMessage());
					Integer answer1NameD = deleteByName(cm.getFirstMessage(), cm.getSecondMessage());
					String[] strNameD = {"DELETE", answer1NameD.toString()};
					answerNameD.add(strNameD);
					//System.out.println(answer.get(1)[3]);
					writeMsg(answerNameD);
					break;
				case ChatMessage.DELETE_YEAR:
					System.out.println("delete by year");
					List<String[]> answerYearD = new ArrayList<String[]>();
					//writeMsg(answer);
					System.out.println("delete by fac" + cm.getFirstMessage() + " " + cm.getSecondMessage());
					Integer answer1YearD = deleteByYear(cm.getFirstMessage(), cm.getSecondMessage());
					String[] strrY = {"DELETE", answer1YearD.toString()};
					System.out.println(strrY[1]);
					answerYearD.add(strrY);
					//System.out.println(answer.get(1)[3]);
					writeMsg(answerYearD);
					break;
				}
			}
			remove(id);
			close();
		}

		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if (sOutput != null)
					sOutput.close();
			} catch (Exception e) {
			}
			try {
				if (sInput != null)
					sInput.close();
			} catch (Exception e) {
			}
			;
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
			}
		}
		/*
		 * 288 Write a String to the Client output stream 289
		 */

		public void createNewFile(String name, List<String> lecturer) {
			String fileSeparator = System.getProperty("file.separator");
			String relativePath = "C:\\Users\\Андрей\\git\\Servertable\\Servertable\\" + name + ".xml";
			File newFile = new File(relativePath);
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			try {
				documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
			} catch (ParserConfigurationException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			String FileName = name + ".xml";

			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(newFile, true));
				bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				bw.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			currentUniversity = new Uni("New Uni");
			Faculty fac = new Faculty(lecturer.get(3));
			Department dep = new Department(lecturer.get(4));
			Lecturer lect = new Lecturer(lecturer.get(0), lecturer.get(1), lecturer.get(2), lecturer.get(5),
					lecturer.get(6), lecturer.get(7));
			currentUniversity.addFaculty(fac);
			fac.addDepartment(dep);
			dep.addLecturer(lect);
			System.out.println(currentUniversity.getFaculty(0).getTitle());
			try {
				DOMExample dom = new DOMExample(currentUniversity, FileName);
			} catch (ParserConfigurationException | TransformerException e1) {
				e1.printStackTrace();
			}

		}

		public void addLect(List<String[]> lecturer) {
			currentUniversity = new Uni("New Uni");
			for (String[] lect : lecturer) {
				Faculty fac = new Faculty(lect[3]);
				Department dep = new Department(lect[4]);
				Lecturer lectuer = new Lecturer(lect[0], lect[1], lect[2], lect[5], lect[6], lect[7]);

				currentUniversity.addFaculty(fac);
				fac.addDepartment(dep);
				dep.addLecturer(lectuer);
			}
			writeMsg(lecturer);
			// System.out.println(currentUniversity.getFaculty(0).getTitle());
			try {
				DOMExample dom = new DOMExample(currentUniversity, fileName);
			} catch (ParserConfigurationException | TransformerException e1) {
				e1.printStackTrace();
			}

		}
		
		public List<String[]> findByFac(String faculty, String degree) {
			SearchController search = new SearchController();
			System.out.println("Uni is workng " + currentUniversity.getFaculty(0).getTitle());
			List<String[]> lecturer = search.listenerSearchByFaculty(currentUniversity, faculty, degree);
			return lecturer;
			//writeMsg(lecturer);
			// System.out.println(currentUniversity.getFaculty(0).getTitle());
		}
		
		public int deleteByFac(String faculty, String degree) {
			DeleteController search = new DeleteController();
			System.out.println("Uni is workng " + currentUniversity.getFaculty(0).getTitle());
			int lecturer = search.listenerSearchByFaculty(currentUniversity, faculty, degree);
			try {
				DOMExample dom = new DOMExample(currentUniversity, fileName);
			} catch (ParserConfigurationException | TransformerException e1) {
				e1.printStackTrace();
			}
			return lecturer;
			//writeMsg(lecturer);
			// System.out.println(currentUniversity.getFaculty(0).getTitle());
		}
		
		public List<String[]> findByName(String department, String name) {
			SearchController search = new SearchController();
			System.out.println("Uni is workng " + currentUniversity.getFaculty(0).getTitle());
			List<String[]> lecturer = search.listenerSearchByName(currentUniversity, department, name);
			return lecturer;
			//writeMsg(lecturer);
			// System.out.println(currentUniversity.getFaculty(0).getTitle());
		}
		
		public int deleteByName(String department, String name) {
			DeleteController search = new DeleteController();
			System.out.println("Uni is workng " + currentUniversity.getFaculty(0).getTitle());
			int lecturer = search.listenerSearchByName(currentUniversity, department, name);
			try {
				DOMExample dom = new DOMExample(currentUniversity, fileName);
			} catch (ParserConfigurationException | TransformerException e1) {
				e1.printStackTrace();
			}
			return lecturer;
		}
		
		public List<String[]> findByYear(String year1, String year2) {
			SearchController search = new SearchController();
			System.out.println("Uni is workng " + currentUniversity.getFaculty(0).getTitle());
			List<String[]> lecturer = search.listenerSearchByYear(currentUniversity, year1, year2);
			return lecturer;
			//writeMsg(lecturer);
			// System.out.println(currentUniversity.getFaculty(0).getTitle());
		}
		
		public int deleteByYear(String year1, String year2) {
			DeleteController search = new DeleteController();
			System.out.println("Uni is workng " + currentUniversity.getFaculty(0).getTitle());
			int lecturer = search.listenerSearchByYear(currentUniversity, year1, year2);
			try {
				DOMExample dom = new DOMExample(currentUniversity, fileName);
			} catch (ParserConfigurationException | TransformerException e1) {
				e1.printStackTrace();
			}
			return lecturer;
		}

		public void openFile(String name) {
			SAXExample sax;
			try {
				sax = new SAXExample(name);
				currentUniversity = sax.uni;
				// there
				System.out.println(currentUniversity.getFaculty(1).getTitle() + " open");
				// writeMsg(name);
				UniversityController unicontr = new UniversityController();
				
				List<String[]> uniList = unicontr.getUniversity(currentUniversity);
				writeMsg(uniList);
				// currentTableWithLecturers.updateTable(currentUniversity);
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		private boolean writeMsg(List<String[]> msg) {
			// if Client is still connected send the message to it
			if (!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch (IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}

		private void turnLeft() {
			UniversityController unicontr = new UniversityController();
			System.out.println(currentUniversity.getFaculty(0).getTitle() + " open");
			String label = null;
			String label2 = null;
			List<String[]> rowList = unicontr.getUniversity(currentUniversity);
			// writeMsg(uniList);
			
			String[][] data = rowList.toArray(new String[0][]);
			/// lost data
			// System.out.println(data[1][1]);
			// System.out.println(t.currentUniversity.getFaculty(0).getTitle());
			if (rowList.size() <= numOfRows) {
				numOfRowsEnd = rowList.size();
				numOfRowsStart = 0;
				label = "Number of elementson page: " + rowList.size()
					 + " from total " + rowList.size();
				label2 =" Number of page: " + 1 + " from " +
						 (rowList.size() / numOfRows + 1);
				/*
				 * lableNumberOfElements .setText("Number of elementson page: " + rowList.size()
				 * + " from total " + rowList.size());
				 * lableNumberOnPage.setText(" Number of page: " + 1 + " from " +
				 * (rowList.size() / numOfRows + 1));
				 */
			} else {
				if (numOfRowsEnd != rowList.size()) {
					if (numOfRowsEnd <= rowList.size() - numOfRows) {
						numOfRowsEnd += numOfRows;
						numOfRowsStart += numOfRows;
						currPage+=1;
						label = " Number of page: " + (currPage) +
								 " from " + (rowList.size() / numOfRows + 1);
						label2 ="Number of elementson page: " + numOfRows + " from total " + rowList.size();
						/*
						 * currPage += 1; lableNumberOnPage.setText( " Number of page: " + (currPage) +
						 * " from " + (rowList.size() / numOfRows + 1)); lableNumberOfElements.setText(
						 * "Number of elementson page: " + numOfRows + " from total " + rowList.size());
						 */
					} else {
						numOfRowsStart = numOfRowsEnd;
						numOfRowsEnd = rowList.size();
						currPage+=1;
						label = " Number of page: " +
								 (rowList.size() / numOfRows + 1) + " from " + (rowList.size() / numOfRows + 1);
						label2 ="Number of elementson page: " +
								 rowList.size() % numOfRows + " from total " + rowList.size();
						/*
						 * currPage += 1; lableNumberOnPage.setText(" Number of page: " +
						 * (rowList.size() / numOfRows + 1) + " from " + (rowList.size() / numOfRows +
						 * 1)); lableNumberOfElements.setText("Number of elementson page: " +
						 * rowList.size() % numOfRows + " from total " + rowList.size());
						 */
					}
				} else {
					numOfRowsEnd = numOfRows;
					numOfRowsStart = 0;
					currPage=1;
					label = " Number of page: " + currPage +
							 " from " + (rowList.size() / numOfRows + 1);
					label2 ="Number of elementson page: " + numOfRows + " from total " +
							 rowList.size();
					/*
					 * currPage = 1; lableNumberOnPage .setText(" Number of page: " + currPage +
					 * " from " + (rowList.size() / numOfRows + 1)); lableNumberOfElements
					 * .setText("Number of elementson page: " + numOfRows + " from total " +
					 * rowList.size());
					 */
				}
			}
			// data = rowList.toArray(new String[0][]);
			List<String[]> dataCurr = new ArrayList<String[]>();
			dataCurr.add(new String[] {"TURN", label, label2});
			for (int i = numOfRowsStart; i < numOfRowsEnd; i++) {
				dataCurr.add(new String[] { (String) data[i][0], (String) data[i][1], (String) data[i][2],
						(String) data[i][3], (String) data[i][4], (String) data[i][5] });
			}
			// String[][] dataCurr1 = dataCurr.toArray(new String[0][]);
			System.out.println(label2 + label);
			writeMsg(dataCurr);
			System.out.println(dataCurr.get(0)[0]);
		}

		private void turnRight() {
			UniversityController unicontr = new UniversityController();
			System.out.println(currentUniversity.getFaculty(0).getTitle() + " open");
			List<String[]> rowList = unicontr.getUniversity(currentUniversity);
			// writeMsg(uniList);
			String label = null;
			String label2 = null;
			String[][] data = rowList.toArray(new String[0][]);
			/// lost data
			// System.out.println(data[1][1]);
			// System.out.println(t.currentUniversity.getFaculty(0).getTitle());
			if (rowList.size() <= numOfRows) {
				numOfRowsEnd = rowList.size();
				numOfRowsStart = 0;
				label = " Number of page: " + 1 + " from " +
						 (rowList.size() / numOfRows + 1);
				label2 = "Number of elementson page: " + rowList.size() + " from total " +
						  rowList.size();
				/*
				 * lableNumberOnPage.setText(" Number of page: " + 1 + " from " +
				 * (rowList.size() / numOfRows + 1)); lableNumberOfElements
				 * .setText("Number of elementson page: " + rowList.size() + " from total " +
				 * rowList.size());
				 */
			} else {
				if (numOfRowsEnd == numOfRows) {
					numOfRowsEnd = rowList.size();
					numOfRowsStart = numOfRowsEnd - rowList.size() % numOfRows;
					currPage = (rowList.size() / numOfRows + 1);
					label = " Number of page: " + currPage + " from " + (rowList.size() /
							numOfRows + 1);
					label2 = "Number of elementson page: "
							+ rowList.size() % numOfRows + " from total " + rowList.size();
					
					/*
					 * currPage = (rowList.size() / numOfRows + 1); lableNumberOnPage
					 * .setText(" Number of page: " + currPage + " from " + (rowList.size() /
					 * numOfRows + 1)); lableNumberOfElements.setText("Number of elementson page: "
					 * + rowList.size() % numOfRows + " from total " + rowList.size());
					 */
				} else {
					if (numOfRowsEnd != rowList.size()) {
						if (numOfRowsEnd >= 2 * numOfRows) {
							numOfRowsEnd -= numOfRows;
							numOfRowsStart -= numOfRows;
						} else {
							numOfRowsStart = 0;
							numOfRowsEnd = numOfRows;
						}
						currPage -= 1;
						label = " Number of page: " + currPage +
								 " from " + (rowList.size() / numOfRows + 1);
						label2 = "Number of elementson page: " + numOfRows + " from total " + rowList.size();
						/*
						 * currPage -= 1; lableNumberOnPage.setText( " Number of page: " + currPage +
						 * " from " + (rowList.size() / numOfRows + 1)); lableNumberOfElements.setText(
						 * "Number of elementson page: " + numOfRows + " from total " + rowList.size());
						 */
					} else {
						numOfRowsEnd = rowList.size() - rowList.size() % numOfRows;
						numOfRowsStart = numOfRowsEnd - numOfRows;
						currPage -= 1;
						label = " Number of page: " + currPage +
								  " from " + (rowList.size() / numOfRows + 1);
						label2 = "Number of elementson page: " + numOfRows + " from total " + rowList.size();
						/*
						 * currPage -= 1; lableNumberOnPage.setText( " Number of page: " + currPage +
						 * " from " + (rowList.size() / numOfRows + 1)); lableNumberOfElements.setText(
						 * "Number of elementson page: " + numOfRows + " from total " + rowList.size());
						 */
					}
				}
			}
			// data = rowList.toArray(new String[0][]);
			List<String[]> dataCurr = new ArrayList<String[]>();
			dataCurr.add(new String[] {
					"TURN", label, label2
			});
			for (int i = numOfRowsStart; i < numOfRowsEnd; i++) {
				dataCurr.add(new String[] { (String) data[i][0], (String) data[i][1], (String) data[i][2],
						(String) data[i][3], (String) data[i][4], (String) data[i][5] });
			}
			// String[][] dataCurr1 = dataCurr.toArray(new String[0][]);
			writeMsg(dataCurr);
			System.out.println(dataCurr.get(0)[0]);
		}
		
		private void goToHead() {
			UniversityController unicontr = new UniversityController();
			System.out.println(currentUniversity.getFaculty(0).getTitle() + " open");
			List<String[]> rowList = unicontr.getUniversity(currentUniversity);
			// writeMsg(uniList);

			String[][] data = rowList.toArray(new String[0][]);
			/*if (rowList.size() > numOfRows) {
				currPage = 1;
				lableNumberOnPage
						.setText(" Number of page: " + currPage + " from " + (rowList.size() / numOfRows + 1));
				lableNumberOfElements
						.setText("Number of elementson page: " + numOfRows + " from total " + rowList.size());
			} else {
				currPage = 1;
				lableNumberOnPage
						.setText(" Number of page: " + currPage + " from " + (rowList.size() / numOfRows + 1));
				lableNumberOfElements
						.setText("Number of elementson page: " + rowList.size() + " from total " + rowList.size());
			}*/
			numOfRowsEnd = numOfRows;
			numOfRowsStart = 0;
			data = rowList.toArray(new String[0][]);
			List<String[]> dataCurr = new ArrayList<String[]>();
			for (int i = numOfRowsStart; i < numOfRowsEnd; i++) {
				dataCurr.add(new String[] { (String) data[i][0], (String) data[i][1], (String) data[i][2],
						(String) data[i][3], (String) data[i][4], (String) data[i][5] });
			}
			writeMsg(dataCurr);
		}
		
		private void goToTail() {
			UniversityController unicontr = new UniversityController();
			System.out.println(currentUniversity.getFaculty(0).getTitle() + " open");
			List<String[]> rowList = unicontr.getUniversity(currentUniversity);
			// writeMsg(uniList);

			String[][] data = rowList.toArray(new String[0][]);
			if (rowList.size() % numOfRows != 0) {
				numOfRowsEnd = rowList.size();
				numOfRowsStart = rowList.size() - rowList.size() % numOfRows;
				/*currPage = (rowList.size() / numOfRows + 1);
				lableNumberOnPage
						.setText(" Number of page: " + currPage + " from " + (rowList.size() / numOfRows + 1));
				lableNumberOfElements.setText("Number of elementson page: " + rowList.size() % numOfRows
						+ " from total " + rowList.size());*/
			} else {
				numOfRowsEnd = rowList.size();
				numOfRowsStart = rowList.size() - numOfRows;
				/*currPage = (rowList.size() / numOfRows + 1);
				lableNumberOnPage
						.setText(" Number of page: " + currPage + " from " + (rowList.size() / numOfRows + 1));
				lableNumberOfElements
						.setText("Number of elementson page: " + numOfRows + " from total " + rowList.size());
*/
			}
			
			data = rowList.toArray(new String[0][]);
			List<String[]> dataCurr = new ArrayList<String[]>();
			for (int i = numOfRowsStart; i < numOfRowsEnd; i++) {
				dataCurr.add(new String[] { (String) data[i][0], (String) data[i][1], (String) data[i][2],
						(String) data[i][3], (String) data[i][4], (String) data[i][5] });
			}
			writeMsg(dataCurr);
		}
	}

}
