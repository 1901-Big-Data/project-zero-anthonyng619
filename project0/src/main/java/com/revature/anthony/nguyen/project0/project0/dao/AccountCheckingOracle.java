package com.revature.anthony.nguyen.project0.project0.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	public boolean deposit(double amt, String bankId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean withdraw(double amt, String bankId) {
		String query = "call withdrawmoney(?, ?)";
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setDouble(1, amt);
			stmt.setString(2, bankId);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			log.catching(e);
			return false;
		}
	}

	@Override
	public boolean transfer(double amt, String sourceBankId, String targetBankId) {
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
		return false;
	}

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

	@Override
	public Optional<AccountChecking> retrieveAccount(String bankAccountId) {
		String query = "select * from ACCOUNTS_CHECKING where BANK_ACCOUNT_ID = ?";
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setString(1, bankAccountId);
			ResultSet rs = stmt.executeQuery();
			AccountChecking account = new AccountChecking();
			if(rs.next()) {
				account.setBankAccountId(rs.getString("BANK_ACCOUNT_ID"));
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
