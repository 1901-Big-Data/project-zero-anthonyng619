package com.revature.anthony.nguyen.project0.project0.model;

public class AccountChecking {
	private String bankAccountId;
	private double balance;
	
	public AccountChecking() {
	}
	
	public AccountChecking(String bankAccountId, double balance) {
		this.bankAccountId = bankAccountId;
		this.balance = balance;
	}
	
	public String getBankAccountId() {
		return this.bankAccountId;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void setBankAccountId(String id) {
		this.bankAccountId = id;
	}
	
	public void setBalance(double amt) {
		this.balance = amt;
	}
}
