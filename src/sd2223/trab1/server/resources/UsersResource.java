package sd2223.trab1.server.resources;

import java.util.List;


import jakarta.inject.Singleton;
import sd2223.trab1.api.User;
import sd2223.trab1.api.java.Users;
import sd2223.trab1.api.rest.UsersService;
import sd2223.trab1.server.javas.JavaUsers;

@Singleton
public class UsersResource extends RestResource implements UsersService {
	final Users impl;

	public UsersResource() {
		this.impl = new JavaUsers();
	}




	@Override
	public String createUser(User user) {
		return super.reTry(impl.createUser(user));
	}

	@Override
	public User getUser(String name, String pwd) {
		return super.reTry(impl.getUser(name, pwd));

	}


	@Override
	public User updateUser(String name, String pwd, User newUser) {
		return super.reTry(impl.updateUser(name, pwd, newUser));

	}

	@Override
	public User deleteUser(String name, String pwd) {
		return super.reTry(impl.deleteUser(name, pwd));
	}

	@Override
	public List<User> searchUsers(String pattern) {
		return super.reTry(impl.searchUsers(pattern));
	}

	public Boolean hasUserNoPwd(String name){return super.reTry((impl.hasUserNoPwd(name)));}


}
