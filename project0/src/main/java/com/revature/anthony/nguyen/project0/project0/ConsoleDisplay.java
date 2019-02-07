package com.revature.anthony.nguyen.project0.project0;

import java.util.Scanner;

public class ConsoleDisplay {
	private Scanner input;
	
	public ConsoleDisplay() {
		input = new Scanner(System.in);
	}
	
	public void start() {
		loginRegister();
	}
	
	public void loginRegister() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    	System.out.println("Menu Options: ");
    	System.out.println("    1. Register a new account");
    	System.out.println("    2. Login to an existing account");
    	System.out.print("\nPick an option: ");
    	String choice = input.next();
    
		switch(choice) {
    	case "1":
    		register();
    		break;
    	case "2":
    		login();
    		break;
    	default:
    		System.out.print("Please pick a valid option.");
    		loginRegister();
    		break;
    	}
	}
	
	public void register() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Registering new account with the bank.");
		System.out.print("Enter your first name: ");
		String firstName = input.nextLine();
		System.out.print("Enter your last name: ");
		String lastName = input.nextLine();
		System.out.print("Enter a username: ");
		String username = input.nextLine();
		
		boolean nameChecking = true;
		while(nameChecking) {
			System.out.println("That username is unavailable. Please enter another one.");
			System.out.print("Enter a username: ");
			username = input.nextLine();
		}
		System.out.print("Enter a password: ");
		String password = input.nextLine(); // TODO: Hash this in the future.
		
		User user = User.get(username, password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setBalance(0.0);
		
		DBConnection.getDBConnection().addUser(user);
		
		
		mainMenu1();
	}
	
	public void login() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Login Page");
		System.out.print("Username:");
		String username = input.nextLine();
		System.out.print("Password:");
		String password = input.nextLine();
		
		boolean validated = false;
		while(!validated) {
			validated = DBConnection.getDBConnection().validateUser(username, password) ? true: false;
			System.out.println("Username or password is incorrect.");
			System.out.print("Username:");
			username = input.nextLine();
			System.out.print("Password:");
			password = input.nextLine();
		}
		
		
		
		mainMenu1();
	}
	
	public void mainMenu1() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		if(User.get() == null) { // Null check 
			register();
			return;
		}
		System.out.println("Welcome, " + User.get().getFirstName() + User.get().getLastName() + "!");
		System.out.println("How can we service you today?");
		
	}
}
