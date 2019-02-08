package com.revature.anthony.nguyen.project0.project0.model;

public class AccountInformation {
	private int bankAccountId;
	private double balance;
	
	public AccountInformation() {
	}
	
	public AccountInformation(int bankId, double balance) {
		this.setBankAccountId(bankId);
		this.setBalance(balance);
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	
	
}
