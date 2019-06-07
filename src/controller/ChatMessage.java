package controller;
import java.io.*;
/*
03
 * This class defines the different type of messages that will be exchanged between the
04
 * Clients and the Server.
05
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no
06
 * need to count bytes or to wait for a line feed at the end of the frame
07
 */
import java.util.List;

public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;
	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	public static final int FILE_NEW = 0;
	public static final int FILE_OPEN = 1;
	public static final int SEARCH_FAC = 2;
	public static final int SEARCH_NAME = 3;
	public static final int SEARCH_YEAR = 4;
	public static final int DELETE_FAC = 5;
	public static final int DELETE_NAME = 6;
	public static final int DELETE_YEAR = 7;
	public static final int ADD_LECT = 8;
	public static final int TURN_LEFT = 9;
	public static final int TURN_RIGHT = 10;
	public static final int GO_TO_HEAD = 11;
	public static final int GO_TO_TAIL = 12;
	public static final int CLOSE_SEARCH = 13;
	private int type;
	private String message1;
	private String message2;
	private List<String> lecturer;
	private List<String[]> uni;
	int numOfRows;
	

	// constructor
	public ChatMessage(int type, String message) {
		this.type = type;
		this.message1 = message;
	}
	
	public ChatMessage(int type, String message1, String message2) {
		this.type = type;
		this.message1 = message1;
		this.message2 = message2;
	}
	
	public ChatMessage(int type, int numOfRows) {
		this.type = type;
		this.numOfRows = numOfRows;
	}
	
	public ChatMessage(int type,  List<String[]> message) {
		this.type = type;
		this.uni = message;
	}
	
	public ChatMessage(int type, String name, List<String> message) {
		this.type = type;
		this.message1 = name;
		this.lecturer = message;
	}

	
	
	// getters
	int getType() {
		return type;
	}
	
	int getNumOfRows() {
		return numOfRows;
	}

	String getFirstMessage() {
		return message1;
	}

	String getSecondMessage() {
		return message2;
	}
	
	List<String> getLecturer(){
		return lecturer;
	}
	
	List<String[]> getUni(){
		return uni;
	}
}
