package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.model.User;

public interface UserDao {
	public Optional<User> addUser(String username, String password, int adminAccess);
	
	public Optional<User> removeUser(User user);
	
	public Optional<User> loginUser(String username, String password);
	
	/*public Optional<User> retrieveUser(String username, String password); */
	
	public boolean checkUsername(String username);
	
	
}
