package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {/*para cada pessoa nova que se conectar, essa classe deve alocar 
										uma thread "ServerTheadManager"*/
	private ServerSocket rcpt;
	private static UserRepository repository = new UserRepository();
	
	public Server(int port) throws IOException{
		this.rcpt = new ServerSocket(port);
	}
	
	public void run() {
		while (true){
			try {
				Socket client = this.rcpt.accept();
				ServerThreadManager multUser = new ServerThreadManager();//falta ajustar cria��o de thread para usar o protocolo novo
				Thread t = new Thread(multUser);//thread for every new client
				t.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
	}

	public static UserRepository getRepository() {
		return repository;
	}

	public static void setRepository(UserRepository repository) {
		Server.repository = repository;
	}
	

}
