package com.revature.anthony.nguyen.project0.project0.model;

public class AccountChecking {
	private int bankAccountId;
	private double balance;
	
	public AccountChecking() {
	}
	
	public AccountChecking(int bankAccountId, double balance) {
		this.bankAccountId = bankAccountId;
		this.balance = balance;
	}
	
	public int getBankAccountId() {
		return this.bankAccountId;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public void setBankAccountId(int id) {
		this.bankAccountId = id;
	}
	
	public void setBalance(double amt) {
		this.balance = amt;
	}
}
