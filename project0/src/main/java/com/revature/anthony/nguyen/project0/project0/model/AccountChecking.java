package com.revature.anthony.nguyen.project0.project0.model;

import java.util.ArrayList;
import java.util.Map;

public class AccountChecking {
	private ArrayList<AccountInformation> accounts;
	private double totalBalance;
	
	public AccountChecking() {
		
	}
	
	public AccountChecking(ArrayList<AccountInformation> accounts, double totalBalance) {
		this.accounts = accounts;
		this.totalBalance = totalBalance;
	}
	
	public ArrayList<AccountInformation> getAccounts() {
		return this.accounts;
	}
	
	public double getTotalBalance() {
		return this.totalBalance;
	}
	
	public void setAccounts(ArrayList<AccountInformation> accounts) {
		this.accounts = accounts;
	}
	
	public void setTotalBalance(double amt) {
		this.totalBalance = amt;
	}
}
