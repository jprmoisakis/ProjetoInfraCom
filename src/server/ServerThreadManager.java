package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThreadManager implements Runnable {
	private Socket client;
	private BufferedReader in;
	private DataOutputStream out;
	private User user;
	
	public ServerThreadManager(Socket client){
		this.client = client;
		startConnection();
	}

	public void run() {//seletor
		while(true){//melhorar isso?
			String input;
			try {
				input = in.readLine();
				switch(input){
					case "signup":
						this.signUp();
						break;
					case "login":
						this.login();
						break;
					case "logout":
						this.logout();
						break;
				}		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	public void signUp(){//cadastra
		try {
			String username = in.readLine();
			String password = in.readLine();
			if(Server.getRepository().getUser(username) != null){
				out.writeBoolean(false);
			}else{
				User user = new User(username,password,"","");
				Server.getRepository().add(user);
				out.writeBoolean(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void login() throws IOException{//loga o usuario
		String username = in.readLine();
		String password = in.readLine();
		
		User user = Server.getRepository().getUser(username);
		if(user != null && password == user.getPassword() && user.getAvaiable() == false){
			Server.getRepository().getUser(username).setIp(client.getInetAddress().getHostAddress());
			Server.getRepository().getUser(username).setPort(in.readLine());
			Server.getRepository().getUser(username).setAvaiable(true);
			this.user = Server.getRepository().getUser(username);
			out.writeBoolean(true);
		}else{
			out.writeBoolean(false);
		}
	}
	
	public void logout() throws IOException{
		String username = in.readLine();
		if(Server.getRepository().getUser(username) != null){
			Server.getRepository().getUser(username).setAvaiable(false);
			out.writeBoolean(true);//desloga o cliente
		}
	}
	
	public void startConnection(){//inicia conexão
		try {
			in=new BufferedReader(new InputStreamReader(client.getInputStream()));
			out= new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			endConnection();//caso desconecte
			e.printStackTrace();
		}
		
	}
	public void endConnection() {//finaliza a conexão
		Server.getRepository().getUser(user.getUsername()).setAvaiable(false);
		try{
			in.close();
			out.close();
			client.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		this.client = null;
		this.in = null;
		this.out = null;
		this.user = null;
		
	}

	
	
	
}
