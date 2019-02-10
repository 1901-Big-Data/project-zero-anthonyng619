package com.revature.anthony.nguyen.project0.project0.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.dao.AccountCheckingDao;
import com.revature.anthony.nguyen.project0.project0.dao.AccountCheckingOracle;
import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;

public class AccountCheckingService {
	private static AccountCheckingService service;
	private AccountCheckingDao dao;
	
	private AccountCheckingService() {
		dao = AccountCheckingOracle.get();
	}
	
	public static AccountCheckingService get() {
		if(service == null) {
			service = new AccountCheckingService();
			return service;
		} else {
			return service;
		}
	}
	
	public Optional<AccountChecking> withdraw(double amt, int bankId, int userId) throws NoSuchElementException {
		return AccountCheckingOracle.get().withdraw(amt, bankId, userId);
	}
	
	public Optional<AccountChecking> deposit(double amt, int bankId, int userId) throws NoSuchElementException {
		return AccountCheckingOracle.get().deposit(amt, bankId, userId);
	}
	
	public Optional<AccountChecking> retrieveAccounts(int userId) throws NoSuchElementException {
		return AccountCheckingOracle.get().retrieveAccounts(userId);
	}
	
	public Optional<AccountInformation> createAccount(int userId) {
		return AccountCheckingOracle.get().createAccount(userId);
	}
	
}
