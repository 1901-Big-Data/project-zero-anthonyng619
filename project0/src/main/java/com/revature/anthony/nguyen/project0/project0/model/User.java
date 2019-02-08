package com.revature.anthony.nguyen.project0.project0.model;

public class User {
	
	private String username;
	transient private String password;
	private int userID;
	private String firstName;
	private String lastName;
	private int adminAccess;
	
	public User() {
		firstName = "";
		lastName = "";
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;           
	}
	
	public User(String username, String password, int adminAccess, int userId) {
		this.username = username;
		this.password = password;
		this.setAdminAccess(adminAccess);
		this.userID = userId;
	}
	
	//---- Getters
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public int getUserID() {
		return this.userID;
	}
	
	public int getAdminAccess() {
		return this.adminAccess;
	}

	//---- Setters
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setAdminAccess(int adminAccess) {
		this.adminAccess = adminAccess;
	}
	
}
