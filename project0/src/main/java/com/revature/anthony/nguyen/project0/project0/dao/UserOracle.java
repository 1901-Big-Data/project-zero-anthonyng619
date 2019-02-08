package com.revature.anthony.nguyen.project0.project0.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.service.UserService;
import com.revature.anthony.nguyen.project0.project0.util.DBConnection;

public class UserOracle implements UserDao{
	private static UserOracle userOracle;
	private Logger log = LogManager.getLogger(UserOracle.class);
	
	private UserOracle() {
	}
	
	public static UserOracle get() {
		if(userOracle == null) {
			userOracle = new UserOracle();
			return userOracle;
		} else {
			return userOracle;
		}
	}
	
	@Override
	public Optional<User> addUser(String username, String password, int adminAccess) {
		String query = "call createuser(?, ?, ?, ?)";
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setInt(3, adminAccess);	
			stmt.registerOutParameter(4, Types.INTEGER);
			stmt.execute();
			
			Integer userid = stmt.getInt(4);
			
			log.debug("Added user to database with user id: " + userid);
			
			// Create user
			User user = new User(username, password, adminAccess, userid);
			return Optional.of(user);
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> removeUser(User user) {
		// TODO: Remove user
		return Optional.empty();
	}

	@Override
	public Optional<User> loginUser(String username, String password) {
		String query = "select * from p0_users where USERNAME = ? and PASSCODE = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				int adminAccess = rs.getInt("adminaccess");
				int userid = rs.getInt("USER_ID");
				User user = new User(username, password, adminAccess, userid);
				return Optional.of(user);
			}
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
		return Optional.empty();
	}

	/*
	@Override
	public Optional<User> retrieveUser(String username, String password) {
		String query = "select * from BANKUSERS where USERNAME = ? and PASSCODE = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSCODE"));
				user.setFirstName(rs.getString("FIRSTNAME"));
				user.setLastName(rs.getString("LASTNAME"));
				user.setBankAccountId(rs.getString("BANK_ACCOUNT_ID"));
				return Optional.of(user);
			}
		} catch(SQLException e) {
			System.err.println("SQL Exception");
			return Optional.empty();
		}
		return Optional.empty();
	}*/
	
	@Override
	public boolean checkUsername(String username) {
		String query = "select * from p0_users where USERNAME = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				log.debug(username + " exists");
				return true;
			}
		} catch(SQLException e) {
			log.catching(e);
			return false;
		}
		return false;
	}

}
