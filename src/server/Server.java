package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private ServerSocket rcpt;
	
	public Server(int port) throws IOException{//start listening
		this.rcpt = new ServerSocket(port);
	}
	
	public void run() {
		while (true){
			try {
				Socket client = this.rcpt.accept();
				ServerThreadManager multUser = new ServerThreadManager(client);
				Thread t = new Thread(multUser);//thread for every new client
				t.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

}
