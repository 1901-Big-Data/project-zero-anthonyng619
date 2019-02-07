package com.revature.anthony.nguyen.project0.project0.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.dao.UserDao;
import com.revature.anthony.nguyen.project0.project0.dao.UserOracle;
import com.revature.anthony.nguyen.project0.project0.model.User;

public class UserService {
	private static UserService service;
	private UserDao userDao;

	/**
	 * Private constructor for DBConnection.
	 */
	private UserService() {
		userDao = UserOracle.get();
	}
	
	/**
	 * Creates a singleton for DBConnection.
	 * @return DBConnection The singleton
	 */
	public static UserService get() {
		if(service == null) {
			service = new UserService();
			return service;
		}
		else {
			return service;
		}
	}
	
	public boolean addUser(User user) {
		return userDao.addUser(user);
	}
	
	public boolean validateUser(String username, String password) {
		return userDao.validateUser(username, password);
	}
	
	public Optional<User> retrieveUser(String username, String password) {
		return userDao.retrieveUser(username, password);
	}
	
	public boolean checkUser(String username) {
		return userDao.checkUsername(username);
	}

}
