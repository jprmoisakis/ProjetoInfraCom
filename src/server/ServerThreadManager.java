package server;

import java.net.Socket;

public class ServerThreadManager implements Runnable {
	private Socket client;
	
	public ServerThreadManager(Socket client){
		this.client = client;
	}
	
	public void auth(){
		
		
	}
	
	public void run() {
		//will do stuff like inputstream, outputstream
		
	}
	
	
	
}
