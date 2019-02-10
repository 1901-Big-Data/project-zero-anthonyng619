package com.revature.anthony.nguyen.project0.project0.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;
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
	public Optional<AccountChecking> deposit(double amt, int bankId, int userId) {
		String query = "call depositmoney(?, ?, ?, ?, ?)";
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setDouble(1, amt);
			stmt.setInt(2, bankId);
			stmt.setInt(3, userId);
			stmt.registerOutParameter(4, Types.DOUBLE);
			stmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			stmt.execute();
			
			Double newBalance = stmt.getDouble(4);
			ResultSet rs = (ResultSet) stmt.getObject(5);
			
			double totalBalance = 0;
			ArrayList<AccountInformation> accountlist = new ArrayList<AccountInformation>();
			boolean rsempty = true; // Used to check if resultset is empty
			
			while(rs.next()) {
				rsempty = false;
				int bankid = rs.getInt("BANK_ACCOUNT_ID");
				double balance = rs.getDouble("BALANCE");
				totalBalance += balance;
				AccountInformation acc_info = new AccountInformation(bankid, balance);
				accountlist.add(acc_info);
			}
			
			if(rsempty) {
				return Optional.empty();
			}
			
			AccountChecking account = new AccountChecking(accountlist, totalBalance);
			return Optional.of(account);
		} catch (SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<AccountChecking> withdraw(double amt, int bankId, int userId) {
		
		String query = "call withdrawmoney(?, ?, ?, ?, ?)";
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setDouble(1, amt);
			stmt.setInt(2, bankId);
			stmt.setInt(3, userId);
			stmt.registerOutParameter(4, Types.DOUBLE);
			stmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			stmt.execute();
			
			Double newBalance = stmt.getDouble(4);
			ResultSet rs = (ResultSet) stmt.getObject(5);
			if(newBalance == -1) {
				return Optional.empty();
			}
			
			double totalBalance = 0;
			ArrayList<AccountInformation> accountlist = new ArrayList<AccountInformation>();
			boolean rsempty = true; // Used to check if resultset is empty
			
			while(rs.next()) {
				rsempty = false;
				int bankid = rs.getInt("BANK_ACCOUNT_ID");
				double balance = rs.getDouble("BALANCE");
				totalBalance += balance;
				AccountInformation acc_info = new AccountInformation(bankid, balance);
				accountlist.add(acc_info);
			}
			
			if(rsempty) {
				return Optional.empty();
			}
			
			AccountChecking account = new AccountChecking(accountlist, totalBalance);
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
	public Optional<AccountChecking> retrieveAccounts(int userId) {
		String query = "select * from p0_checking where user_id = ?";
		try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(query)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			ArrayList<AccountInformation> accountlist = new ArrayList<AccountInformation>();
			double totalBalance = 0;
			
			boolean rsempty = true; // Used to check if resultset is empty
			
			while(rs.next()) {
				rsempty = false;
				int bankid = rs.getInt("BANK_ACCOUNT_ID");
				double balance = rs.getDouble("BALANCE");
				totalBalance += balance;
				AccountInformation acc_info = new AccountInformation(bankid, balance);
				accountlist.add(acc_info);
			}
			
			if(rsempty) {
				return Optional.empty();
			}
			
			AccountChecking account = new AccountChecking(accountlist, totalBalance);
			return Optional.of(account);
		} catch(SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<AccountInformation> createAccount(int userId) {
		String query = "call createcheckingaccount(?, ?)";
		try(CallableStatement stmt = DBConnection.get().getConnection().prepareCall(query)) {
			stmt.setInt(1, userId);
			stmt.registerOutParameter(2, Types.INTEGER);
			
			stmt.execute();
			
			Integer bankid = stmt.getInt(2);

			AccountInformation account = new AccountInformation(bankid, 0.0);
			
			return Optional.of(account);
		} catch (SQLException e) {
			log.catching(e);
			return Optional.empty();
		}
	}
	
}
