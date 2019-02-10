package com.revature.anthony.nguyen.project0.project0.application;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;
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
	    		System.exit(0);
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
		
		try {
			user = UserService.get().addUser(username, password, 0).get();
		} catch (NoSuchElementException e) {
			System.out.println("Username is taken.");
			return;
		}
		log.debug("User finished registering for new account. Username: "+username);
		
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
		
		try {
			user = UserService.get().loginUser(username, password).get();
		} catch (NoSuchElementException e) {
			System.out.println("Wrong username or passowrd.");
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
			System.out.println("2. Open banking accounts");
			System.out.println("3. Change address");
			System.out.println("4. Change password");
			System.out.println("\n0. Log out");
			
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				bankingAccounts();
				break;
			case "2":
				openBankAccount();
				break;
			case "0":
				breaker = true;
				user = null;
				accountChecking = null;
				break;
			default:
				break;
			}
		}
	}
	
	/*
	public void bankingOptions() throws NoSuchElementException {
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
				
				try {
					accountChecking = AccountCheckingService.get().withdraw(Double.parseDouble(amt), user.getBankAccountId()).get();
				} catch (NoSuchElementException e) {
					System.out.println("You don't have enough money to withdraw that much!");
					accountChecking = AccountCheckingService.get().retrieveAccount(user.getBankAccountId()).get();
				}
				
				displayBalance();
				break;
			case "2":
				System.out.println("How much would you like to deposit?");
				amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().deposit(Double.parseDouble(amt), user.getBankAccountId()).get();
				} catch (NoSuchElementException e) {
					accountChecking = AccountCheckingService.get().retrieveAccount(user.getBankAccountId()).get();
				}
				
				displayBalance();
				break;
			case "0":
				breaker = true;
				break;
			default:
				break;
			}
		}
	}*/
	
	public void bankingAccounts() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		try {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			accountChecking = AccountCheckingService.get().retrieveAccounts(user.getUserID()).get();
			System.out.println("These are your available accounts: ");

		
			int page = 0;
			int pageSize = 5;
			boolean breaker = false;
			
			while(!breaker) {
			int index = 0;
			while(index < pageSize) {
				AccountInformation acc_info = accountChecking.getAccounts().get(index + page*pageSize);
				System.out.println((index+1 + page*pageSize)+".     |Account number: " + acc_info.getBankAccountId() + " || Balance: " + acc_info.getBalance());
				if(index + 1 + page*pageSize >= accountChecking.getAccounts().size()) {
					break;
				}
				index++;
			}
			if(page == 0) {
				System.out.println("\n8. Main Menu"
						+ "\n9. Next");
			} else {
				System.out.println("\n8. Previous\n9. Next");
			}
			System.out.println("Which account would you like to access?");
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				int indexInList = page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankAccountInvocation(indexInList);
				break;
				
			case "2":
				indexInList = 1 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankAccountInvocation(indexInList);
				break;
				
			case "3":
				indexInList = 2 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankAccountInvocation(indexInList);
				break;
				
			case "4":
				indexInList = 3 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankAccountInvocation(indexInList);
				break;
				
			case "5":
				indexInList = 4 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
			
				bankAccountInvocation(indexInList);
				break;
				
			case "8":
				index = 0;
				if(page > 0) {
					page--;
				} else {
					return;
				}
				break;
			case "9": 
				index = 0;
				if(!((page+1)*pageSize > accountChecking.getAccounts().size())) {
					page++;
				}
				break;
			
			default:
				break;
			}
			}
		} catch(NoSuchElementException e) {
			log.catching(e);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("We apologize for the inconvenience. It looks like you do not have any opened accounts with us.");
			System.out.println("Please open an account with us at the main menu.");
			System.out.println("Press any button to return back to the main menu...");
			
			input.nextLine();
			return;
		}
	}
	
	public void bankAccountInvocation(int bankId) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		AccountInformation acc_info = accountChecking.getAccounts().get(bankId);
		System.out.println("Currently viewing Bank Account Number: " + acc_info.getBankAccountId());
		System.out.println("Remaining balance: "+ acc_info.getBalance());
		
	
		boolean breaker = false;

		while(!breaker) {
			System.out.println("What would you like to do?\n");
			System.out.println("1. Withdraw");
			System.out.println("2. Deposit");
			System.out.println("3. Transfer");
			System.out.println("\nPick an option: ");
			
			String choice = input.nextLine();
			
			switch(choice) {
			case "1":
				System.out.println("How much would you like to withdraw?");
				String amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().withdraw(Double.parseDouble(amt), acc_info.getBankAccountId(), user.getUserID()).get();
					System.out.println("You withdrew $" + amt);
					System.out.println("Your remaining balance is $" + accountChecking.getAccounts().get(bankId).getBalance());
					breaker = true;
					System.out.println("Press enter to continue.");
					amt = input.nextLine();
				} catch (NoSuchElementException e) {
					System.out.println("You don't have enough money to withdraw that much!");
					accountChecking = AccountCheckingService.get().retrieveAccounts(user.getUserID()).get();
				} 
				break;
			case "2":
				System.out.println("How much would you like to deposit?");
				amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().deposit(Double.parseDouble(amt), acc_info.getBankAccountId(), user.getUserID()).get();
					System.out.println("You deposited $" + amt);
					System.out.println("Your remaining balance is $" + accountChecking.getAccounts().get(bankId).getBalance());
					breaker = true;
					System.out.println("Press enter to continue.");
					amt = input.nextLine();
				} catch (NoSuchElementException e) {
					System.out.println("You can't deposit that much!");
					accountChecking = AccountCheckingService.get().retrieveAccounts(user.getUserID()).get();
				} 
				break;
			}
			
		}
		
	}
	
	public void openBankAccount() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("You have the choice to open either a checking or savings account.");
		System.out.println("Note: You can set up multiple accounts with us!");
		System.out.println("\n1. Create a new checking account.");
		System.out.println("\n2. Create a new savings account.");
		
		boolean breaker = false;
		while(!breaker) {
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				try {
					AccountInformation account = AccountCheckingService.get().createAccount(user.getUserID()).get();
					System.out.println("Your checking account has been made.\n Your checking account id is "+ account.getBankAccountId()+".");
					System.out.println("\nPress any key to return to main menu...");
					input.nextLine();
					breaker = true;
				} catch(NoSuchElementException e) {
					log.catching(e);
					System.out.println("You have lost connection.");
					return;
				}
				break;
			case "2":
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
		//System.out.println("Your balance: $" + accountChecking.getBalance());
	}
}
