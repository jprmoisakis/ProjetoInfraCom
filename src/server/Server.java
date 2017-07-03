package server;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


import reliableUDP.receiver;

public class Server implements Runnable {/*para cada pessoa nova que se conectar, essa classe deve alocar 
										uma thread "ServerTheadManager"*/
	private static UserRepository repository = new UserRepository();
	private List<MsgManager> msgManagerRepository;
	private receiver receiver;
	private byte[] array;
	
	public Server() throws SocketException{
		this.receiver = new receiver(5001,0);
		this.array = new byte[10000];
		this.msgManagerRepository = new ArrayList<MsgManager>();
	}

	public void run() {
		while (true){
			try {
				String input;
				long size = receiver.receive(array);
				input = new String(array, StandardCharsets.UTF_8);
				String hostName = receiver.getHostName();
				if(containsHostName(msgManagerRepository,hostName)){	 // caso já exista no repositório, coloca a mensagem na fila, ou seja já tem uma thread consumindo dessa fila
					findMsgManager(msgManagerRepository,hostName).getQueue().put(input);//coloca a mensagem na fila
					
				}else{
					MsgManager tableQueue = new MsgManager(hostName);//cria hostname associado a uma queue
					msgManagerRepository.add(tableQueue);
					int index = msgManagerRepository.indexOf(tableQueue);
					msgManagerRepository.get(index).getQueue().put(input);//coloca a mensagem na fila
					ServerThreadManager multUser = new ServerThreadManager(msgManagerRepository.get(index).getQueue(),hostName);
					Thread t = new Thread(multUser);
					t.start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	
	public static boolean containsHostName(List<MsgManager> list, String hostName) {
	    for (MsgManager object : list) {
	        if (object.getHostName() == hostName) {
	            return true;
	        }
	    }
	    return false;
	}
	public static MsgManager findMsgManager(List<MsgManager> list, String hostName) {
	    for (MsgManager object : list) {
	        if (object.getHostName() == hostName) {
	            return object;
	        }
	    }
	    return null;
	}
}