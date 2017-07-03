package server;

import java.util.concurrent.BlockingQueue;

public class MsgManager {//classe criada com o

	private String hostName;
	private BlockingQueue<String> queue;
	
	public MsgManager(String hostName){
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public BlockingQueue<String> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
}
