package com.revature.anthony.nguyen.project0.project0.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.revature.anthony.nguyen.project0.project0.dao.AccountCheckingDao;
import com.revature.anthony.nguyen.project0.project0.dao.AccountCheckingOracle;
import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;

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
	
	public Optional<AccountChecking> withdraw(double amt, int bankId) throws NoSuchElementException {
		return AccountCheckingOracle.get().withdraw(amt, bankId);
	}
	
	public Optional<AccountChecking> retrieveAccount(int bankId) {
		return AccountCheckingOracle.get().retrieveAccount(bankId);
	}
}
