package server;

public class User {//User basic data
	private String login;
	private boolean avaiable;
	private String ip;
	private String port;
	
	public User(String login, boolean avaiable, String ip, String port) {
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
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
}
