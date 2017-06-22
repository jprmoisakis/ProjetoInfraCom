package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerThreadManager implements Runnable {
	private Socket client;
	private BufferedReader in;
	
	public ServerThreadManager(Socket client){
		this.client = client;
	}
	
	public void signUp(){
		try {
			String username = in.readLine();
			String password = in.readLine();
			User user = new User(username,password,"","");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void auth(){
		
		
	}
	
	
	
	public void run() {
		//will do stuff like inputstream, outputstream
		
	}
	
	
	
}
