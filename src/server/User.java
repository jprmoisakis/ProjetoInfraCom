package server;

public class User {//User basic data
	private String username;
	private String password;
	private boolean avaiable;
	private String ip;
	private int port;
	
	public User(String username,String password, String ip, int port) {
		this.username = username;
		this.password = password;
		this.ip = ip;
		this.port = port;
		this.avaiable = false;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String login) {
		this.username = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean getAvaiable() {
		return avaiable;
	}
	public void setAvaiable(boolean avaiable) {
		this.avaiable = avaiable;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
