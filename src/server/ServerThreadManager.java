package server;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;	
import reliableUDP.sender;

public class ServerThreadManager implements Runnable {
	private User user;
	private sender sender;
	private BlockingQueue<String> queue;
	private String hostName;
	
	public ServerThreadManager(BlockingQueue<String> queue,String hostName) throws SocketException{
		this.user= null;
		this.queue = queue;
		this.hostName = hostName;
	}

	public void run() {//seletor
		while(true){//melhorar isso?
			String input;
			try {
				input = queue.take();
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	public void signUp() throws InterruptedException{//cadastra
		String username = queue.take();
		String password = queue.take();
		
		if(Server.getRepository().getUser(username) != null){
			sendMsg("duplicateuser");
		}else{
			User user = new User(username,password,"");
			Server.getRepository().add(user);
			sendMsg("signupok");
		}	
	}
	
	public void login() throws IOException, InterruptedException{//loga o usuario
		String username = queue.take();
		String password = queue.take();
		
		User user = Server.getRepository().getUser(username);
		if(user != null && password == user.getPassword()){
			if(user.getAvaiable() == false){
				Server.getRepository().getUser(username).setIp(hostName);
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
	
	public void logout() throws IOException, InterruptedException{
		String username = queue.take();
		if(Server.getRepository().getUser(username) != null){
			Server.getRepository().getUser(username).setAvaiable(false);
			sendMsg("logout");
		}
	}
	
	public void sendMsg(String message){//envia o estado da ação
		byte[] array = message.getBytes(StandardCharsets.UTF_8);
		try {
			sender.sendBytes(array);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutDown(){
		
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
