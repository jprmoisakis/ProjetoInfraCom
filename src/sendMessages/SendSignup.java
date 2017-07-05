package sendMessages;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import chat.sender;
import reliableUDP.receiver;

public class SendSignup implements Runnable{
	private String username;
	private String password;
	private String ip;
	public SendSignup(String username, String password, String ip){
		this.username = username;
		this.password = password;
		this.ip = ip;
	}
	public void run(){
		
			try {
				sender envia = new sender(ip, 5002);
				
				byte[] action = ("signup").getBytes(StandardCharsets.UTF_8);
				envia.sendBytes(action);
				
				byte[] arrayUser = username.getBytes(StandardCharsets.UTF_8);
				envia.sendBytes(arrayUser);
				
				byte[] arrayPsswd = username.getBytes(StandardCharsets.UTF_8);
				envia.sendBytes(arrayPsswd);
				
				Thread.sleep(500);
				receiver receiver = new receiver(5002,0);
				byte[] rcv =  new byte[10000];
				long size = receiver.receive(rcv);
				String input = new String(rcv, StandardCharsets.UTF_8);
				if(input != "signupok"){
					//lançar exceção de usuário ja cadastrado
				}
				envia.close();
	
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		
		
	}
}
