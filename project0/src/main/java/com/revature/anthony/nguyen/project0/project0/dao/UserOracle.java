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
	public Optional<User> addUser(String username, String password, String firstname, String lastname) {
		String query = "call insertIntoUser(?, ?, ?, ?, ?, ?)";
		System.out.println("SQL: Running query - "+query );
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, firstname);
			stmt.setString(4, lastname);	
			stmt.registerOutParameter(5, Types.INTEGER);
			stmt.registerOutParameter(6, Types.INTEGER);
			stmt.execute();
			
			Integer userid = stmt.getInt(5);
			Integer bankid = stmt.getInt(6);
			
			log.debug("Added user to database with user id: " + userid);
			
			// Create user
			User user = new User(username, password, firstname, lastname, userid, bankid);
			return Optional.of(user);
		} catch(SQLException e) {
			System.out.println("SQL Exception");
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
		String query = "select * from BANKUSERS where USERNAME = ? and PASSCODE = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				String firstName = rs.getString("FIRSTNAME");
				String lastName = rs.getString("LASTNAME");
				int userid = rs.getInt("USER_ID");
				int bankid = rs.getInt("BANK_ACCOUNT_ID");
				User user = new User(username, password, firstName, lastName, userid, bankid);
				return Optional.of(user);
			}
		} catch(SQLException e) {
			System.err.println("SQL Exception");
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
		String query = "select * from BANKUSERS where USERNAME = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				System.out.println(username + " exists");
				return true;
			}
		} catch(SQLException e) {
			System.err.println("SQL Exception");
			return false;
		}
		return false;
	}

}
