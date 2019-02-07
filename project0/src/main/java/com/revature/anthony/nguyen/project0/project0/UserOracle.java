package com.revature.anthony.nguyen.project0.project0;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserOracle implements UserDao{
	private static UserOracle userOracle;
	
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
	public boolean addUser(User user) {
		String query = "call insertIntoUser(?, ?, ?, ?)";
		System.out.println("SQL: Running query - "+query );
		try(CallableStatement stmt = UserService.get().getConnection().prepareCall(query)) {
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());			
			stmt.execute();
			System.out.println("Added user to database!");
			return true;
		} catch(SQLException e) {
			System.out.println("SQL Exception");
			return false;
		}
	}

	@Override
	public boolean removeUser(User user) {
		// TODO: Remove user
		return false;
	}

	@Override
	public boolean validateUser(String username, String password) {
		String query = "select * from BANKUSERS where USERNAME = ? and ";
		
		try(PreparedStatement stmt = UserService.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch(SQLException e) {
			System.err.println("SQL Exception");
			return false;
		}
		return false;
	}

	@Override
	public boolean checkUsername(String username) {
		String query = "select * from BANKUSERS where USERNAME = ?";
		
		try(PreparedStatement stmt = UserService.get().getConnection().prepareStatement(query)) {
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
