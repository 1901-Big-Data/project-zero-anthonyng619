package com.revature.anthony.nguyen.project0.project0.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBConnection {
	private static DBConnection dbcon;
	private Connection conn;
	private Properties props;
	private Logger logger;
	private boolean isAdmin = false;
	
	private DBConnection() {
		this.props = new Properties();
		logger = LogManager.getLogger(DBConnection.class);
	}
	
	public static DBConnection get() {
		if(dbcon == null) {
			dbcon = new DBConnection();
			return dbcon;
		} else {
			return dbcon;
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
        	
        	String superusername = this.props.getProperty("dbsuperuser");
        	String superpassword = this.props.getProperty("dbsuperpassword");
        	
        	String dbUrl = "jdbc:oracle:thin:" + superusername + "/" + superpassword + "@" + this.props.getProperty("database") + ":1521:ORCL";
        	this.conn = DriverManager.getConnection(dbUrl);
        	if(conn != null) {
        		System.out.println("Connection successfully established.");
        		this.isAdmin = true;
        		return true;
        	}
        } catch (SQLTimeoutException e) {
        	logger.debug("Timeout value exceeded the limit");
        } catch (SQLException e) {
        	logger.debug("Failed to authenticate as a super user. Accessing as a normal user.");
        	try {
        		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            	
            	// Grab config file that stores db information
            	loadProperties();
            	
            	String username = this.props.getProperty("dbuser");
            	String password = this.props.getProperty("dbpassword");
            	
            	String dbUrl = "jdbc:oracle:thin:" + username + "/" + password + "@" + this.props.getProperty("database") + ":1521:ORCL";
            	this.conn = DriverManager.getConnection(dbUrl);
            	if(conn != null) {
            		System.out.println("Connection successfully established.");
            		this.isAdmin = false;
            		return true;
            	}
        	} catch (SQLTimeoutException e1) {
        		logger.catching(e1);
        	} catch (SQLException e1) {
        		logger.catching(e1);
        	}
        }
		return false;
	}
	
	private void loadProperties() {
		try {
			this.props.load(new FileInputStream("src/main/resources/config.properties"));
		} catch(NullPointerException e) {
			logger.debug("Please provide a valid config file");
		} catch(FileNotFoundException e) {
			logger.debug("Invalid file");
		} catch(Exception e) {
			logger.debug("Reached an error loading config");
		}
	}
	
	public Connection getConnection() {
		return this.conn;
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}
}
