package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.model.User;

public interface UserDao {
	public boolean addUser(User user);
	
	public boolean removeUser(User user);
	
	public boolean validateUser(String username, String password);
	
	public Optional<User> retrieveUser(String username, String password);
	
	public boolean checkUsername(String username);
	
	
}
