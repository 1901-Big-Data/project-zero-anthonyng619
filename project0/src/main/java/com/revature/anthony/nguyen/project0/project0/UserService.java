package com.revature.anthony.nguyen.project0.project0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserService {
	private static UserService dbConn;
	private UserDao userDao;
	private Connection conn;
	private Properties props;
	private Logger logger;

	/**
	 * Private constructor for DBConnection.
	 */
	private UserService() {
		this.props = new Properties();
		logger = LogManager.getRootLogger();
		userDao = UserOracle.get();
	}
	
	/**
	 * Creates a singleton for DBConnection.
	 * @return DBConnection The singleton
	 */
	public static UserService get() {
		if(dbConn == null) {
			dbConn = new UserService();
			return dbConn;
		}
		else {
			return dbConn;
		}
	}
	
	/** 
	 * connect
	 * 
	 * Attempts to connect to the database provided.
	 * @return boolean True or false depending if connection was established.
	 */
	public boolean connect() {
		try {
        	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        	
        	// Grab config file that stores db information
        	loadProperties();
        	
        	String dbUrl = "jdbc:oracle:thin:" + this.props.getProperty("dbuser") + "/" + this.props.getProperty("dbpassword") + "@" + this.props.getProperty("database") + ":1521:ORCL";
        	System.out.println(dbUrl);
        	this.conn = DriverManager.getConnection(dbUrl);
        	if(conn != null) {
        		System.out.println("Connection successfully established.");
        		return true;
        	}
        } catch (SQLTimeoutException e) {
        	logger.debug("Timeout value exceeded the limit");
        } catch (SQLException e) {
        	logger.debug("Database access error or url is null");
        }
		return false;
	}
	
	private void loadProperties() {
		try {
			this.props.load(new FileInputStream("src/main/config.properties"));
		} catch(NullPointerException e) {
			logger.debug("Please provide a valid config file");
		} catch(FileNotFoundException e) {
			logger.debug("Invalid file");
		} catch(Exception e) {
			logger.debug("Reached an error loading config");
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
	
	public Connection getConnection() {
		return this.conn;
	}
}
