package controller;
import java.io.*;
import java.util.ArrayList;
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
	public static final int SEARCH = 2;
	public static final int DELETE = 3;
	private int type;
	private String message;
	private List<String> lecturer;
	private List<String[]> uni;

	// constructor
	public ChatMessage(int type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public ChatMessage(int type,  List<String[]> message) {
		this.type = type;
		this.uni = message;
	}
	
	public ChatMessage(int type, String name, List<String> message) {
		this.type = type;
		this.message = name;
		this.lecturer = message;
	}

	
	
	// getters
	int getType() {
		return type;
	}

	String getMessage() {
		return message;
	}
	
	List<String> getLecturer(){
		return lecturer;
	}
	
	List<String[]> getUni(){
		return uni;
	}
}
