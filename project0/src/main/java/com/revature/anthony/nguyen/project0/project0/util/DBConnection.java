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
}
