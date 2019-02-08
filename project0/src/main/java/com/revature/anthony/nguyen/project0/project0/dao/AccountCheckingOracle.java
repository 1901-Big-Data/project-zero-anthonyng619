package com.revature.anthony.nguyen.project0.project0.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.util.DBConnection;

public class AccountCheckingOracle implements AccountCheckingDao {
	private static AccountCheckingOracle accountCheckingOracle;
	private Logger log = LogManager.getLogger(AccountCheckingOracle.class);
	
	private AccountCheckingOracle() {
	}
	
	public static AccountCheckingOracle get() {
		if(accountCheckingOracle == null) {
			accountCheckingOracle = new AccountCheckingOracle();
			return accountCheckingOracle;
		} else {
			return accountCheckingOracle;
		}
	}
	
	@Override
	public Optional<AccountChecking> deposit(double amt, int bankId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<AccountChecking> withdraw(double amt, int bankId) {
		String query = "call withdrawmoney(?, ?, ?)";
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setDouble(1, amt);
			stmt.setInt(2, bankId);
			stmt.registerOutParameter(3, Types.DOUBLE);
			stmt.execute();
			
			Double newBalance = stmt.getDouble(3);
			
			AccountChecking account = new AccountChecking(bankId, newBalance);
			return Optional.of(account);
		} catch (SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<AccountChecking> transfer(double amt, int sourceBankId, int targetBankId) {
		/*String query = "select BALANCE from ACCOUNTSCHECKING where BANK_ACCOUNT_ID = ?";
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, bankAccountId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				
			}
		} catch (SQLException e) {
			log.catching(e);
			return false;
		}
		return false;
		*/
		return Optional.empty();
	}

	/*
	@Override
	public Optional<Double> getBalance(String bankAccountId) {
		String query = "select BALANCE from ACCOUNTS_CHECKING where BANK_ACCOUNT_ID = ?";
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, bankAccountId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return Optional.of(rs.getDouble("BALANCE"));
			}
		} catch (SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
		return Optional.empty();
	}
	 */
	@Override
	public Optional<AccountChecking> retrieveAccount(int bankAccountId) {
		String query = "select * from ACCOUNTS_CHECKING where BANK_ACCOUNT_ID = ?";
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setInt(1, bankAccountId);
			ResultSet rs = stmt.executeQuery();
			AccountChecking account = new AccountChecking();
			if(rs.next()) {
				account.setBankAccountId(rs.getInt("BANK_ACCOUNT_ID"));
				account.setBalance(rs.getDouble("BALANCE"));
				return Optional.of(account);
			}
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
		return Optional.empty();
	}
	
}
