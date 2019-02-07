package com.revature.anthony.nguyen.project0.project0;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection {
	private static DBConnection dbConn;
	private Connection conn;
	private Properties props;
	private Logger logger;

	/**
	 * Private constructor for DBConnection.
	 */
	private DBConnection() {
		this.props = new Properties();
		logger = LogManager.getRootLogger();
	}
	
	/**
	 * Creates a singleton for DBConnection.
	 * @return DBConnection The singleton
	 */
	public static DBConnection getDBConnection() {
		if(dbConn == null) {
			dbConn = new DBConnection();
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
		String query = "insert into ACCOUNTS (USERNAME, USER_PASSWORD, FIRST_NAME, LAST_NAME, BALANCE) VALUES('" + user.getUsername() + "','" + user.getPassword() + "','" + user.getFirstName() + "','" + user.getLastName() + "'," + user.getBalance() + ")";
		logger.debug("SQL: Running query - "+query );
		try(Statement stmt = this.conn.createStatement()) {
			stmt.executeUpdate(query);
			logger.debug("Added user to database!");
			return true;
		} catch(SQLException e) {
			logger.debug("SQL Exception");
			return false;
		}
	}
	
	public void removeUser() {
		// TODO: SQL query to remove user
	}
	
	public boolean validateUser(String username, String encryptedPassword) {
		String query = "select * from ACCOUNTS where USERNAME = '" + username + "'";
		
		try(Statement stmt = this.conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				if(rs.getString("USER_PASSWORD").equals(encryptedPassword)) {
					return true;
				}
				return false;
			}
		} catch(SQLException e) {
			logger.debug("SQL Exception");
			return false;
		}
		return false;
	}
	
	public boolean checkUser(String username) {
		logger.debug("Checking if username exists: "+username);
		String query = "select * from ACCOUNTS where USERNAME = '" + username + "'";
		
		try(Statement stmt = this.conn.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		} catch(SQLException e) {
			logger.debug("SQL Exception");
			return false;
		}
		return false;
	}
	
	
	
	
	public Connection getConnection() {
		return this.conn;
	}
}
