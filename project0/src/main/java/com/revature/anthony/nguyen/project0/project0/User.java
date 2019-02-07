package com.revature.anthony.nguyen.project0.project0;

public class User {
	private static User user;
	
	private String username;
	private String password;
	private String userID;
	private String bankAccountID;
	private String firstName;
	private String lastName;
	private String phone;
	private double balance;
	
	private User() {
		firstName = "";
		lastName = "";
		balance = 0.0;
	}
	
	private User(String username, String password) {
		this.username = username;
		this.password = password;
		setFirstName(firstName);
		setLastName(lastName);
		setBalance(balance);
	}
	
	/**
	 * get
	 * 
	 * 
	 */
	public static User get() {
		if(user != null) {
			return user;
		} else {
			return null;
		}
	}
	
	/** 
	 * get
	 * 
	 * Grabs the only instance of the singleton
	 * @param String username
	 * @param String password
	 */
	public static User get(String username, String password) {
		if(user != null) {
			return user;
		} else {
			user = new User(username, password);
			return user;
		}
	}
	
	public void setup() {
		// TODO: Setup name, phone, balance, all from database
	}
	

	
	/** 
	 * addBalance
	 * 
	 * This method adds balance to the user
	 * 
	 * @param amt This is the amount to add
	 * @return boolean Returns a boolean indicating if balance has been changed clientside.
	 */
	public boolean addBalance(double amt) {
		double tentativeBalance = getBalance() + amt;
		if(tentativeBalance < Double.MAX_VALUE && tentativeBalance >= 0.0) {
			setBalance(tentativeBalance);
			return true;
		}
		return false;
	}
	
	/** 
	 * minusBalance
	 * 
	 * This method adds balance to the user
	 * 
	 * @param amt This is the amount to add
	 * @return boolean Returns a boolean indicating if balance has been changed clientside.
	 */
	public boolean minusBalance(double amt) {
		double tentativeBalance = getBalance() - amt;
		if(tentativeBalance >= 0.0) {
			setBalance(tentativeBalance);
			return true;
		} 
		return false;
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
	
	public String getPhone() {
		return this.phone;
	}
	
	public double getBalance() {
		return this.balance;
	}

	//---- Setters
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
