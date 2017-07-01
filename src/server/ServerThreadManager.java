package server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import reliableUDP.receiver;
import reliableUDP.sender;

public class ServerThreadManager implements Runnable {
	private User user;
	private receiver receiver;
	private sender sender;
	private byte[] array;
	
	public ServerThreadManager(){
		this.user= null;
		this.array = new byte[10000];
		this.receiver = new receiver(5001,0);
		//startConnection();
	}

	public void run() {//seletor
		while(true){//melhorar isso?
			String input;
			try {
				long bytes = receiver.receive(array);
				this.sender = new sender(receiver.getHostName(),5001);//depois de receber o pacote define pra quem será feito o envio
				input = new String(array, StandardCharsets.UTF_8);
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
			long bytesUsername = receiver.receive(array);
			String username = new String(array, StandardCharsets.UTF_8);
			
			long bytesPassword = receiver.receive(array);
			String password = new String(array, StandardCharsets.UTF_8);
			
			if(Server.getRepository().getUser(username) != null){
				sendMsg("duplicateuser");
			}else{
				User user = new User(username,password,"");
				Server.getRepository().add(user);
				sendMsg("signupok");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void login() throws IOException{//loga o usuario
		long bytesUsername = receiver.receive(array);
		String username = new String(array, StandardCharsets.UTF_8);
		
		long bytesPassword = receiver.receive(array);
		String password = new String(array, StandardCharsets.UTF_8);
		
		User user = Server.getRepository().getUser(username);
		if(user != null && password == user.getPassword()){
			if(user.getAvaiable() == false){
				Server.getRepository().getUser(username).setIp(this.receiver.getHostName());
				Server.getRepository().getUser(username).setAvaiable(true);
				this.user = Server.getRepository().getUser(username);
				sendMsg("logged");
			}else{
				sendMsg("alreadylogged");
			}

		}else{
			sendMsg("loginfailed");
		}
	}
	
	public void logout() throws IOException{
		long bytes = receiver.receive(array);
		String username = new String(array, StandardCharsets.UTF_8);
		if(Server.getRepository().getUser(username) != null){
			Server.getRepository().getUser(username).setAvaiable(false);
			sendMsg("logout");
		}
	}
	public void sendMsg(String message){//envia o estado da ação
		array = message.getBytes(StandardCharsets.UTF_8);
		try {
			sender.sendBytes(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*	
	public void startConnection(){//inicia conexão
		try {
			in=new BufferedReader(new InputStreamReader(client.getInputStream()));
			out= new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			endConnection();//caso desconecte
			e.printStackTrace();
		}
		
	}
		
	public void endConnection() {//finaliza a conexão//
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
*/
	
	
	
}
