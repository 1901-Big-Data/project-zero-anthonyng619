package com.revature.anthony.nguyen.project0.project0.application;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;
import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.model.Users;
import com.revature.anthony.nguyen.project0.project0.service.AccountCheckingService;
import com.revature.anthony.nguyen.project0.project0.service.UserService;

public class ConsoleDisplay {
	private Scanner input;
	private User user;
	private Users users;
	private AccountChecking accountChecking;
	private Logger log = LogManager.getLogger(ConsoleDisplay.class);
	private boolean isAdmin;
	
	
	public ConsoleDisplay() {
		input = new Scanner(System.in);
	}
	
	public void start(boolean isAdmin) {
		this.isAdmin = isAdmin;
		mainMenu();
	}
	
	public void loginRegister() {
		boolean breaker = false;
		while(!breaker) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	    	System.out.println("Menu Options: ");
	    	System.out.println("    1. Register a new account");
	    	System.out.println("    2. Login to an existing account");
	    	if(isAdmin) {
	    		System.out.println("    3. Admin Menu");
	    	}
	    	System.out.println("\n    0. Exit");
	    	System.out.println("\nPick an option: ");
	    	String choice = input.nextLine();
	    
			switch(choice) {
	    	case "1":
	    		register();
	    		break;
	    	case "2":
	    		login();
	    		break;
	    	case "3":
	    		if(isAdmin) {
	    			adminMenu();
	    		} else {
	    			System.out.println("Please pick a valid option.");
	    		}
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
		
		System.out.println("Enter your first name: ");
		String firstName = input.nextLine();
		if(firstName.equals("")) return;
		
		System.out.println("Enter your last name: ");
		String lastName = input.nextLine();
		if(lastName.equals("")) return;
		
		System.out.println("Enter a username: ");
		String username = input.nextLine();
		if(username.equals("")) return;
		
		while(UserService.get().checkUser(username)) {
			System.out.println("That username is unavailable. Please enter another one.");
			System.out.println("Enter a username: ");
			username = input.nextLine();
			if(username.equals("")) return;
		}
		System.out.println("Enter a password: ");
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
		boolean breaker = false;
		while(!breaker) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Login Page");
			System.out.print("Username:");
			String username = input.nextLine();
			System.out.print("Password:");
			String password = input.nextLine();
			if(username.equals("") && password.equals("")) return;
			
			try {
				user = UserService.get().loginUser(username, password).get();
				mainMenu();
			} catch (NoSuchElementException e) {
				boolean breaker2 = false;
				while(!breaker2) {
					System.out.println("Wrong username or passowrd.");
					System.out.println("Would you like to try again? (Y/N)" );
					String choice = input.nextLine().toUpperCase();
					switch(choice) {
					case "Y":
						breaker2 = true;
						break;
					case "N":
						breaker = true;
						breaker2 = true;
						break;
					default:
						break;
					}
				}
			}
		}
	}
	
	public void mainMenu() {
		boolean breaker = false;
		while(!breaker) {
			if(user == null || user.getUserID() == 0) {
				loginRegister();
			}
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
			System.out.println("How can we service you today?");
			System.out.println("\nOptions: ");
			System.out.println("1. View banking accounts");
			System.out.println("2. Open banking accounts");
			System.out.println("3. Close user account");
			//System.out.println("3. Change address");
			//System.out.println("4. Change password");
			System.out.println("\n0. Log out");
			
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				bankingAccounts();
				break;
			case "2":
				openBankAccount();
				break;
			case "3":
				closeAccount();
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
	
	
	public void bankingAccounts() {
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
				System.out.println("    " + (index+1)+".  |  Account number: " + acc_info.getBankAccountId() + " || Balance: " + acc_info.getBalance());
				if(index + 1 + page*pageSize >= accountChecking.getAccounts().size()) {
					break;
				}
				index++;
			}
			if(page == 0) {
				if((page+1)*pageSize >= accountChecking.getAccounts().size()) {
					System.out.println("\n9. Main Menu");
				} else {
					System.out.println("\n9. Main Menu\n0. Next");
				}
			} else {
				if((page+1)*pageSize >= accountChecking.getAccounts().size()) {
					System.out.println("\n9. Previous");
				} else {
					System.out.println("\n9. Previous\n0. Next");
				}
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
				
			case "9":
				index = 0;
				if(page > 0) {
					page--;
				} else {
					return;
				}
				break;
			case "0": 
				index = 0;
				if((page+1)*pageSize < accountChecking.getAccounts().size()) {
					page++;
				}
				break;
			
			default:
				break;
			}
			}
		} catch(NoSuchElementException e) {
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
			System.out.println("4. Delete This Checking Account");
			System.out.println("\n0. Go back");
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
			case "3":
				System.out.println("Who would you like to transfer money to?\nEnter the bank account id:");
				String targetidstring = input.nextLine();
				int targetid;
				try {
					targetid = Integer.parseInt(targetidstring);
				} catch(NumberFormatException e) {
					System.out.println("Please enter a valid bank account number.");
					break;
				}
				System.out.println("How much would you like to send?");
				amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().transfer(Double.parseDouble(amt), acc_info.getBankAccountId(), targetid, user.getUserID()).get();
					System.out.println("You transferred $" + amt + " to the bank account id " + targetid + ".");
					System.out.println("Your remaining balance is $" + accountChecking.getAccounts().get(bankId).getBalance());
				} catch(NoSuchElementException e) {
					System.out.println("Your transfer has failed due to insufficient funds or invalid recipient.");
				}
				
				break;
			case "0":
				return;
			case "4":
				boolean breaker2 = false;
				while(!breaker2) {
					System.out.println("Are you sure you want to delete this account?");
					choice = input.nextLine();
					switch(choice) {
					case "y": 
						// Delete account
						try {
							accountChecking = AccountCheckingService.get().deleteAccount(acc_info.getBankAccountId(), user.getUserID()).get();
							breaker2 = true;
							breaker = true;
							System.out.println("You have successfully deleted your account!");
						} catch(NoSuchElementException e) { 
							System.out.println("You are unable to delete the account!\nPlease check if your balance is 0 before deleting.");
						}
						break;
					}
				}
				break;
			default:
				break;
			}
		}
		
	}
	
	public void openBankAccount() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("You have the choice to open either a checking or savings account.");
		System.out.println("Note: You can set up multiple accounts with us!");
		System.out.println("\n1. Create a new checking account.");
		//System.out.println("\n2. Create a new savings account.");
		
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
					System.out.println("You have lost connection.");
					return;
				}
				break;
			case "0":
				breaker = true;
				break;
			default:
				break;
			}
		}
	}

	
	public void adminMenu() {
		boolean breaker = false;
		while(!breaker) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("This is the admin menu.");
			System.out.println("Available Options:\n"); 
			System.out.println("    1. Manage checking accounts");
			System.out.println("    2. Manage user accounts");
			System.out.println("    0. Return to login page");
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				adminBankingAccounts();
				break;
			case "2":
				adminUserAccounts();
				break;
			case "0":
				users = null;
				accountChecking = null;
				user = null;
				breaker = true;
				break;
			}
		}
	}
	
	public void adminBankingAccounts() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		try {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			accountChecking = AccountCheckingService.get().retrieveAccountsAsAdmin().get();
			System.out.println("This is the list of all checking accounts: ");

		
			int page = 0;
			int pageSize = 8;
			boolean breaker = false;
			
			while(!breaker) {
			int index = 0;
			while(index < pageSize) {
				AccountInformation acc_info = accountChecking.getAccounts().get(index + page*pageSize);
				System.out.println("    " + (index+1)+".  |  Account number: " + acc_info.getBankAccountId() + " || Balance: " + acc_info.getBalance());
				if(index + 1 + page*pageSize >= accountChecking.getAccounts().size()) {
					break;
				}
				index++;
			}
			if(page == 0) {
				if((page+1)*pageSize >= accountChecking.getAccounts().size()) {
					System.out.println("\n9. Main Menu");
				} else {
					System.out.println("\n9. Main Menu\n0. Next");
				}
			} else {
				if((page+1)*pageSize >= accountChecking.getAccounts().size()) {
					System.out.println("\n9. Previous");
				} else {
					System.out.println("\n9. Previous\n0. Next");
				}
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
				
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "2":
				indexInList = 1 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "3":
				indexInList = 2 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "4":
				indexInList = 3 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
				
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "5":
				indexInList = 4 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
			
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "6":
				indexInList = 5 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
			
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "7":
				indexInList = 6 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
			
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "8":
				indexInList = 7 + page*pageSize;
				if(1 + indexInList > accountChecking.getAccounts().size()) {
					break;
				}
				breaker = true;
			
				bankInvocationAsAdmin(indexInList);
				break;
				
			case "9":
				index = 0;
				if(page > 0) {
					page--;
				} else {
					return;
				}
				break;
			case "0": 
				index = 0;
				if((page+1)*pageSize < accountChecking.getAccounts().size()) {
					page++;
				}
				break;
			
			default:
				break;
			}
			}
		} catch(NoSuchElementException e) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Sorry, there are no checking accounts in the database.");
			System.out.println("Press any button to return back to the main menu...");
			
			input.nextLine();
			return;
		}
	}
	
	public void bankInvocationAsAdmin(int index) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		AccountInformation acc_info = accountChecking.getAccounts().get(index);
		System.out.println("Currently viewing Bank Account Number: " + acc_info.getBankAccountId());
		System.out.println("Remaining balance: "+ acc_info.getBalance());
		
	
		boolean breaker = false;

		while(!breaker) {
			System.out.println("What would you like to do?\n");
			System.out.println("1. Withdraw");
			System.out.println("2. Deposit");
			System.out.println("3. Transfer");
			System.out.println("9. Delete");
			System.out.println("\n4. Go back");
			System.out.println("\nPick an option: ");
			
			String choice = input.nextLine();
			
			switch(choice) {
			case "1":
				System.out.println("How much would you like to withdraw?");
				String amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().withdraw(Double.parseDouble(amt), acc_info.getBankAccountId(), -1).get();
					System.out.println("You withdrew $" + amt);
					System.out.println("Your remaining balance is $" + accountChecking.getAccounts().get(index).getBalance());
					breaker = true;
					System.out.println("Press enter to continue.");
					amt = input.nextLine();
				} catch (NoSuchElementException e) {
					System.out.println("You don't have enough money to withdraw that much!");
					accountChecking = AccountCheckingService.get().retrieveAccountsAsAdmin().get();
				} 
				break;
			case "2":
				System.out.println("How much would you like to deposit?");
				amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().deposit(Double.parseDouble(amt), acc_info.getBankAccountId(), -1).get();
					System.out.println("You deposited $" + amt);
					System.out.println("Your remaining balance is $" + accountChecking.getAccounts().get(index).getBalance());
					breaker = true;
					System.out.println("Press enter to continue.");
					amt = input.nextLine();
				} catch (NoSuchElementException e) {
					System.out.println("You can't deposit that much!");
					accountChecking = AccountCheckingService.get().retrieveAccountsAsAdmin().get();
				} 
				break;
			case "3":
				System.out.println("Who would you like to transfer money to?\nEnter the bank account id:");
				String targetidstring = input.nextLine();
				int targetid;
				try {
					targetid = Integer.parseInt(targetidstring);
				} catch(NumberFormatException e) {
					System.out.println("Please enter a valid bank account number.");
					break;
				}
				System.out.println("How much would you like to send?");
				amt = input.nextLine();
				
				try {
					accountChecking = AccountCheckingService.get().transfer(Double.parseDouble(amt), acc_info.getBankAccountId(), targetid, -1).get();
					System.out.println("You transferred $" + amt + " to the bank account id " + targetid + ".");
					System.out.println("Your remaining balance is $" + accountChecking.getAccounts().get(index).getBalance());
				} catch(NoSuchElementException e) {
					System.out.println("Your transfer has failed due to insufficient funds or invalid recipient.");
				}
				
				break;
			case "4":
				return;
			case "9":
				boolean breaker2 = false;
				while(!breaker2) {
					System.out.println("Are you sure you want to delete this account?");
					choice = input.nextLine();
					switch(choice) {
					case "y": 
						// Delete account
						try {
							accountChecking = AccountCheckingService.get().deleteAccountAsAdmin(acc_info.getBankAccountId()).get();
							breaker2 = true;
							breaker = true;
							System.out.println("You have successfully deleted your account!");
							System.out.println("Press enter to return back to the main menu");
						} catch(NoSuchElementException e) { 
							System.out.println("You are unable to delete the account!\nPlease check if you are an admin.");
						}
						break;
					}
				}
				break;
			default:
				break;
			}
		}
	}
	
	public void adminUserAccounts() {
		try {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			users = UserService.get().retrieveUsersAsAdmin().get();
			System.out.println("This is the list of all user accounts: ");
		
			int page = 0;
			int pageSize = 8;
			boolean breaker = false;
			
			while(!breaker) {
			int index = 0;
			while(index < pageSize) {
				User user_info = users.getUsers().get(index + page*pageSize);
				System.out.println((index+1)+".     | Username: " + user_info.getUsername());
				if(index + 1 + page*pageSize >= users.getUsers().size()) {
					break;
				}
				index++;
			}
			if(page == 0) {
				if((page+1)*pageSize >= users.getUsers().size()) {
					System.out.println("\n9. Main Menu");
				} else {
					System.out.println("\n9. Main Menu\n0. Next");
				}
			} else {
				if((page+1)*pageSize >= users.getUsers().size()) {
					System.out.println("\n9. Previous");
				} else {
					System.out.println("\n9. Previous\n0. Next");
				}
			}
			System.out.println("Which account would you like to access?");
			String choice = input.nextLine();
			switch(choice) {
			case "1":
				int indexInList = page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
				
				userInvocationAsAdmin(indexInList);
				break;
				
			case "2":
				indexInList = 1 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
				
				userInvocationAsAdmin(indexInList);
				break;
				
			case "3":
				indexInList = 2 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
				
				userInvocationAsAdmin(indexInList);
				break;
				
			case "4":
				indexInList = 3 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
				
				userInvocationAsAdmin(indexInList);
				break;
				
			case "5":
				indexInList = 4 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
			
				userInvocationAsAdmin(indexInList);
				break;
				
			case "6":
				indexInList = 5 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
			
				userInvocationAsAdmin(indexInList);
				break;
				
			case "7":
				indexInList = 6 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
			
				userInvocationAsAdmin(indexInList);
				break;
				
			case "8":
				indexInList = 7 + page*pageSize;
				if(1 + indexInList > users.getUsers().size()) {
					break;
				}
				breaker = true;
			
				userInvocationAsAdmin(indexInList);
				break;
				
			case "9":
				index = 0;
				if(page > 0) {
					page--;
				} else {
					return;
				}
				break;
			case "0": 
				index = 0;
				if((page+1)*pageSize < users.getUsers().size()) {
					page++;
				}
				break;
			
			default:
				break;
			}
			}
		} catch(NoSuchElementException e) {
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Sorry, there are no users in the database.");
			System.out.println("Press any button to return back to the main menu...");
			
			input.nextLine();
			return;
		}
	};
	
	public void userInvocationAsAdmin(int index) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		User user_info = users.getUsers().get(index);
		System.out.println("Currently viewing user: " + user_info.getUsername());		
	
		boolean breaker = false;

		while(!breaker) {
			System.out.println("What would you like to do?\n");
			System.out.println("1. Delete");
			System.out.println("\n0. Go back");
			System.out.println("\nPick an option: ");
			
			String choice = input.nextLine();
			
			switch(choice) {
			case "1":
				boolean breaker2 = false;
				while(!breaker2) {
					System.out.println("Are you sure you want to delete this account? (Y/N)");
					choice = input.nextLine().toUpperCase();
					switch(choice) {
					case "Y": 
						// Delete account
						try {
							users = UserService.get().removeUserAsAdmin(user_info.getUserID()).get();
							breaker2 = true;
							breaker = true;
							System.out.println("You have successfully deleted that user!");
							System.out.println("Press enter to return back to the main menu");
						} catch(NoSuchElementException e) { 
							System.out.println("You are unable to delete the account!\nPlease check if you are an admin.");
						}
						break;
					}
				}
				break;
			case "0":
				return;
			default:
				break;
			}
		}
	}
	
	
	public void closeAccount() {
		System.out.println("To delete your account, please make sure that you withdrew all your money from each account");
		boolean breaker = false;
		while(!breaker) {
			System.out.println("Are you sure you want to delete your account? (Y/N)"); 
			String choice = input.nextLine();
			switch(choice) {
			case "y":
				try {
					user = UserService.get().removeUser(user.getUserID()).get();
					if(user.getUserID() == 0) {
						System.out.println("Your account has been successfully closed.");
						System.out.println("Press the enter key to continue...");
						input.nextLine();
					}
					breaker = true;
				} catch(NoSuchElementException e) {
					System.out.println("Sorry. You can only close your user account that have no balance in any checking accounts.");
				}
				break;
			case "n":
				breaker = true;
				break;
			default:
				break;
			}
		}
	}
}
