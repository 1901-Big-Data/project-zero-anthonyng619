package com.revature.anthony.nguyen.project0.project0;

public class User {
	
	private String username;
	private String password;
	private String userID;
	private String bankAccountID;
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
	
	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getBankAccountId() {
		return this.bankAccountID;
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
	
	public void setBankAccountId(String id) {
		this.bankAccountID = id;
	}
	
}
