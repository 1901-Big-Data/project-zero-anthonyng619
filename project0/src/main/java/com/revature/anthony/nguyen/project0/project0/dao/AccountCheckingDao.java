package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;

public interface AccountCheckingDao {
	public boolean deposit(double amt, String targetBankId);
	public boolean withdraw(double amt, String bankId);
	public boolean transfer(double amt, String sourceBankId, String targetBankId);
	public Optional<Double> getBalance(String bankAccountId);
	public Optional<AccountChecking> retrieveAccount(String bankAccountId);
}
