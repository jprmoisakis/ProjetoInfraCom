package sendMessages;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import chat.sender;
import reliableUDP.receiver;

public class sendLogout implements Runnable{
	private String username;
	private String ip;
	public sendLogout(String username, String password, String ip){
		this.username = SendLogin.getUsernameSave();
		this.ip = ip;
	}
	public void run(){
		
			try {
				sender envia = new sender(ip, 5002);
				
				byte[] action = ("logout").getBytes(StandardCharsets.UTF_8);
				envia.sendBytes(action);
				
				byte[] arrayUser = username.getBytes(StandardCharsets.UTF_8);
				envia.sendBytes(arrayUser);
				
				Thread.sleep(500);
				receiver receiver = new receiver(5002,0);
				byte[] rcv =  new byte[10000];
				long size = receiver.receive(rcv);
				String input = new String(rcv, StandardCharsets.UTF_8);
				if(input != "logout"){
					//lançar exceção de não foi possivel deslogar
				}
				envia.close();
	
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		
		
	}
}
