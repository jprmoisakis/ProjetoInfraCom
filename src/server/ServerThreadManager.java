package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThreadManager implements Runnable {
	private Socket client;
	private BufferedReader in;
	private DataOutputStream out;
	
	public ServerThreadManager(Socket client){
		this.client = client;
	}

	public void run() {
		while(true){//melhorar isso?
			String input;
			try {
				input = in.readLine();
				switch(input){
					case "signup":
						this.signUp();
						break;
					case "login":
						this.auth();
						break;
					case "logoff":
						this.logoff();
						break;
				}		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	public void signUp(){
		try {
			String username = in.readLine();
			String password = in.readLine();
			if(Server.getRepository().exists(username)){
				out.writeBoolean(false);
			}else{
				User user = new User(username,password,"","");
				out.writeBoolean(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void auth(){
			
	}
	
	public void login(){
		
	}
	
	public void logoff(){
		
	}
	
	

	
	
	
}
