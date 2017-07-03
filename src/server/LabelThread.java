package server;

import javax.swing.JTextPane;


public class LabelThread implements Runnable {
	
	private JTextPane textPane;
	private UserRepository repo;
	
	public LabelThread(JTextPane textPane, UserRepository repo){
		this.textPane = textPane;
		this.repo = repo;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.textPane.setText(this.repo.onlineUsersListToString());
		}
	}

}
