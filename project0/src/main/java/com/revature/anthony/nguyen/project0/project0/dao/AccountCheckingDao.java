package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;

public interface AccountCheckingDao {
	public Optional<AccountChecking> deposit(double amt, int targetBankId);
	public Optional<AccountChecking> withdraw(double amt, int bankId);
	public Optional<AccountChecking> transfer(double amt, int sourceBankId, int targetBankId);
	public Optional<AccountChecking> retrieveAccount(int bankAccountId);
}
