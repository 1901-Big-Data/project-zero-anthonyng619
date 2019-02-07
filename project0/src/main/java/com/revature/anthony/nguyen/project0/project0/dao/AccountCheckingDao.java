package com.revature.anthony.nguyen.project0.project0.dao;

import java.util.Optional;

public interface AccountCheckingDao {
	public boolean deposit(double amt);
	public boolean withdraw(double amt);
	public boolean transfer(double amt, String bankAccountId);
	public Optional<Double> getBalance();
}
