package server;

public class User {//User basic data
	private String login;
	private boolean avaiable;
	private String ip;
	private int port;
	
	public User(String login, String ip, int port) {
		this.login = login;
		this.ip = ip;
		this.port = port;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public boolean isAvaiable() {
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
