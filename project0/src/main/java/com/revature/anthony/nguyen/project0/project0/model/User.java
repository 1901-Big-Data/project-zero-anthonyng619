package com.revature.anthony.nguyen.project0.project0.model;

public class User {
	
	private String username;
	transient private String password;
	private int userID;
	private int bankAccountID;
	private String firstName;
	private String lastName;
	
	public User() {
		firstName = "";
		lastName = "";
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;           
	}
	
	public User(String username, String password, String firstName, String lastName, int userId, int bankId) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userID = userId;
		this.bankAccountID = bankId;
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

	public int getBankAccountId() {
		return this.bankAccountID;
	}
	
	public int getUserID() {
		return this.userID;
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
	
	public void setBankAccountId(int id) {
		this.bankAccountID = id;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
