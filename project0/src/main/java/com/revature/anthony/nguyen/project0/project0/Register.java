package com.revature.anthony.nguyen.project0.project0;

import java.sql.*;

public class Register {
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	
	public Register(String firstName, String lastName, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		System.out.println("Your new account has been created");
	}
	

}
