package com.revature.anthony.nguyen.project0.project0.application;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.service.AccountCheckingService;
import com.revature.anthony.nguyen.project0.project0.service.UserService;

public class ConsoleDisplay {
	private Scanner input;
	private User user;
	private AccountChecking accountChecking;
	private Logger log = LogManager.getLogger(ConsoleDisplay.class);
	
	public ConsoleDisplay() {
		input = new Scanner(System.in);
	}
	
	public void start() {
		mainMenu();
	}
	
	public void loginRegister() {
		boolean breaker = false;
		while(!breaker) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	System.out.println("Menu Options: ");
	    	System.out.println("    1. Register a new account");
	    	System.out.println("    2. Login to an existing account");
	    	System.out.println("\n    0. Exit");
	    	System.out.print("\nPick an option: ");
	    	String choice = input.nextLine();
	    
			switch(choice) {
	    	case "1":
	    		register();
	    		break;
	    	case "2":
	    		login();
	    		break;
	    	case "0":
	    		breaker = true;
	    		break;
	    	default:
	    		System.out.println("Please pick a valid option.");
	    		break;
	    	}
		}
	}
	
	public void register() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Registering new account with the bank.");
		
		System.out.print("Enter your first name: ");
		String firstName = input.nextLine();
		if(firstName.equals("")) return;
		
		System.out.print("Enter your last name: ");
		String lastName = input.nextLine();
		if(lastName.equals("")) return;
		
		System.out.print("Enter a username: ");
		String username = input.nextLine();
		if(username.equals("")) return;
		
		while(UserService.get().checkUser(username)) {
			System.out.println("That username is unavailable. Please enter another one.");
			System.out.print("Enter a username: ");
			username = input.nextLine();
			if(username.equals("")) return;
		}
		System.out.print("Enter a password: ");
		String password = input.nextLine(); // TODO: Hash this in the future.
		if(password.equals("")) return;
		
		user = new User(username, password, firstName, lastName);

		UserService.get().addUser(user);
		log.debug("User finished registering for new account. Username: "+username);
		
		try {
			user = UserService.get().retrieveUser(username, password).get();
			accountChecking = AccountCheckingService.get().retrieveAccount(user.getBankAccountId()).get();
		} catch (NoSuchElementException e) {
			log.debug("Cannot retrieve user from sql read.");
			return;
		}
		mainMenu();
	}
	
	public void login() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("Login Page");
		System.out.print("Username:");
		String username = input.nextLine();
		System.out.print("Password:");
		String password = input.nextLine();
		if(username.equals("") && password.equals("")) return;
		while(!UserService.get().validateUser(username, password)) {
			
			System.out.println("Username or password is incorrect.");
			System.out.print("Username:");
			username = input.nextLine();
			System.out.print("Password:");
			password = input.nextLine();
			if(username.equals("") && password.equals("")) return;
		}
		try {
			user = UserService.get().retrieveUser(username, password).get();
			accountChecking = AccountCheckingService.get().retrieveAccount(user.getBankAccountId()).get();
			log.debug("User logged in. Username: "+username);
		} catch (NoSuchElementException e) {
			log.debug("Cannot retrieve user from sql read.");
			return;
		}
		mainMenu();
	}
	
	public void mainMenu() {
		boolean breaker = false;
		while(!breaker) {
			if(user == null) {
				loginRegister();
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
			System.out.println("How can we service you today?");
			System.out.println("\nOptions: ");
			System.out.println("1. View banking accounts");
			System.out.println("2. Change address");
			System.out.println("3. Change password");
			System.out.println("\n0. Log out");
			
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				bankingAccounts();
				break;
			case "0":
				breaker = true;
			default:
				break;
			}
		}
	}
	
	public void bankingAccounts() {
		boolean breaker = false;
		while(!breaker) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			displayBalance();
			System.out.println("How would you like to proceed?");
			System.out.println("1. Withdraw funds");
			System.out.println("2. Deposit funds");
			System.out.println("3. Transfer funds");
			System.out.println("\n0. Go back");
			
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				System.out.println("How much would you like to withdraw?");
				String amt = input.nextLine();
				// Need to catch exceptions...
				AccountCheckingService.get().withdraw(Double.parseDouble(amt), accountChecking.getBankAccountId());
				accountChecking = AccountCheckingService.get().retrieveAccount(user.getBankAccountId()).get();
				displayBalance();
				break;
			case "0":
				breaker = true;
				break;
			default:
				break;
			}
		}
	}
	
	public void displayBalance() {
		System.out.println("Your balance: $" + accountChecking.getBalance());
	}
}
