package com.revature.anthony.nguyen.project0.project0.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;
import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.model.Users;
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
	
	/**
	 * addUser
	 * Queries into the database and sets up a User model wrapped in an optional
	 * to be sent back to the console application
	 * @param String username
	 * @param String password
	 * @param int admin access; 0 or 1 or 2
	 * @return Optional
	 */
	@Override
	public Optional<User> addUser(String username, String password, int adminAccess) {
		// Check username valid
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		boolean hasSpecialChar = p.matcher(username).find();
		if(hasSpecialChar) {
			return Optional.empty();
		}
		
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

	/**
	 * removeUser
	 * Removes the user from the database when balance is 0 across all checking accounts.
	 * @param int userid
	 * @return Optional
	 */
	@Override
	public Optional<User> removeUser(int userid) {
		String query = "call deleteuser(?, ?)";
		
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setInt(1, userid);
			stmt.registerOutParameter(2, Types.INTEGER);
			stmt.execute();
			
			Integer result = stmt.getInt(2);
			if(result == 0) {
				return Optional.empty();
			}
			
			User blankuser = new User();
			return Optional.of(blankuser);
			
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}
	
	/**
	 * removeUserAsAdmin
	 * Force removes a user regardless of balance in the user's accounts. WILL BE WIPED.
	 * @param int userid
	 * @return Optional
	 */
	@
	Override
	public Optional<Users> removeUserAsAdmin(int userid) {
		String query = "call deleteuserasadmin(?, ?)";
		
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setInt(1, userid);
			stmt.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
			stmt.execute();
		
			ResultSet rs = (ResultSet) stmt.getObject(2);
			
			ArrayList<User> userslist = new ArrayList<User>();
			boolean rsempty = true; // Used to check if resultset is empty
			
			while(rs.next()) {
				//System.out.println("Looped");
				rsempty = false;
				int userid_ = rs.getInt("user_id");
				String username = rs.getString("username");
				User user_info = new User(username, "", 0, userid_);
				userslist.add(user_info);
			}
			
			if(rsempty) {
				//return Optional.empty();
			}
			
			Users users = new Users(userslist);
			rs.close();
			return Optional.of(users);
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}

	/**
	 * loginUser
	 * Queries the username and password to DB and returns a User wrapped in an optional
	 * upon success.
	 * @param String username
	 * @param String password
	 * @return Optional
	 */
	@Override
	public Optional<User> loginUser(String username, String password) {
		String query = "select * from p0_users where USERNAME = ? and PASSCODE = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username.toLowerCase());
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
	
	/**
	 * checkUsername
	 * Checks username to see if it contains valid characters or if the username
	 * already exists on the database.
	 * @param String username
	 * @return boolean
	 */
	@Override
	public boolean checkUsername(String username) {
		Pattern p = Pattern.compile("[^a-zA-Z0-9]");
		boolean hasSpecialChar = p.matcher(username).find();
		if(hasSpecialChar) {
			return true;
		}
		String query = "select * from p0_users where USERNAME = ?";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, username.toLowerCase());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				log.debug(username + " exists");
				rs.close();
				return true;
			}
		} catch(SQLException e) {
			log.catching(e);
			return false;
		}
		return false;
	}

	/**
	 * retrieveUsersAsAdmin
	 * Grabs the list of users into a Users model that only the admin has access to.
	 * @return Optional
	 */
	@Override
	public Optional<Users> retrieveUsersAsAdmin() {
		String query = "select *  from p0_users";
		
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<User> list = new ArrayList<User>();
			
			boolean empty = true;
			while(rs.next()) {
				empty = false;
				int userid = rs.getInt("user_id");
				String username = rs.getString("username");
				User tempuser = new User(username, "", 0, userid);
				list.add(tempuser);
			}
		
			if(empty) {
				rs.close();
				return Optional.empty();
			}
		
			Users users = new Users(list);
			rs.close();
			return Optional.of(users);
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}

}
