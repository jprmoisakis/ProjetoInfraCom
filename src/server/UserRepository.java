package server;

public class UserRepository {// Manage users
	private User[] users;
	private int index;
	
	public UserRepository(){
		this.users = new User[10000];
		this.index = 0;
	}

	public User[] getUsers() {
		return users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void add(User user){
		this.users[this.index] = user;
		this.index++;
	}
	
	
}
