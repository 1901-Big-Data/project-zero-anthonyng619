package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.model.Users;

public interface UserDao {
	public Optional<User> addUser(String username, String password, int adminAccess);
	
	public Optional<User> removeUser(int userid);
	
	public Optional<Users> removeUserAsAdmin(int userid);
	
	public Optional<User> loginUser(String username, String password);
	
	public Optional<Users> retrieveUsersAsAdmin();
	
	public boolean checkUsername(String username);
}
