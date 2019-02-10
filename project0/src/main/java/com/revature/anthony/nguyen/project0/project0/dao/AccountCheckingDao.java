package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;

public interface AccountCheckingDao {
	public Optional<AccountChecking> deposit(double amt, int targetBankId, int userId);
	public Optional<AccountChecking> withdraw(double amt, int bankId, int userId);
	public Optional<AccountChecking> transfer(double amt, int sourceBankId, int targetBankId);
	public Optional<AccountChecking> retrieveAccounts(int userId);
	public Optional<AccountInformation> createAccount(int userId);
}
