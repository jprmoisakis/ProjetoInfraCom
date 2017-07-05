package sendMessages;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import chat.sender;
import reliableUDP.receiver;

public class SendLogin implements Runnable{
	private String username;
	private String password;
	private static String usernameSave;
	private String ip;
	public SendLogin(String username, String password, String ip){
		this.username = username;
		this.password = password;
		this.ip = ip;
	}
	public void run(){
		
			try {
				sender envia = new sender(ip, 5002);
				
				byte[] action = ("login").getBytes(StandardCharsets.UTF_8);
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
				if(input != "logged"){
					//lançar exceção de senha ou usuario incorreto
				}
				usernameSave = username;
				envia.close();
	
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		
		
	}
	public static String getUsernameSave() {
		return usernameSave;
	}
	public void setUsernameSave(String usernameSave) {
		this.usernameSave = usernameSave;
	}
}
