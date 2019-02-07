package com.revature.anthony.nguyen.project0.project0;

import java.util.Scanner;

public class ConsoleDisplay {
	private Scanner input;
	private User user;
	
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
    	String choice = input.nextLine();
    
		switch(choice) {
    	case "1":
    		register();
    		break;
    	case "2":
    		login();
    		break;
    	default:
    		System.out.println("Please pick a valid option.");
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
		
		while(UserService.get().checkUser(username)) {
			System.out.println("That username is unavailable. Please enter another one.");
			System.out.print("Enter a username: ");
			username = input.nextLine();
		}
		System.out.print("Enter a password: ");
		String password = input.nextLine(); // TODO: Hash this in the future.
		
		user = new User(username, password, firstName, lastName);
		
		UserService.get().addUser(user);
		
		
		mainMenu1();
	}
	
	public void login() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Login Page");
		System.out.print("Username:");
		String username = input.nextLine();
		System.out.print("Password:");
		String password = input.nextLine();
		
		while(!UserService.get().validateUser(username, password)) {
			
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
		System.out.println("Welcome, " + user.getFirstName() + user.getLastName() + "!");
		System.out.println("How can we service you today?");
		
	}
}
