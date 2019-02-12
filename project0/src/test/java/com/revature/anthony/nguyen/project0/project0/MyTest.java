package com.revature.anthony.nguyen.project0.project0;

import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.Ignore;
import org.junit.Rule;

import static org.junit.Assert.assertEquals;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.NoSuchElementException;

import com.revature.anthony.nguyen.project0.project0.model.AccountChecking;
import com.revature.anthony.nguyen.project0.project0.model.AccountInformation;
import com.revature.anthony.nguyen.project0.project0.model.User;
import com.revature.anthony.nguyen.project0.project0.model.Users;
import com.revature.anthony.nguyen.project0.project0.service.AccountCheckingService;
import com.revature.anthony.nguyen.project0.project0.service.UserService;
import com.revature.anthony.nguyen.project0.project0.util.DBConnection;

import junit.framework.TestCase;

public class MyTest extends TestCase {
	public static UserService userService = UserService.get();	
	public static AccountCheckingService accountService = AccountCheckingService.get();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	/*
	 * Testing UserOracle
	 */
	
	@Test
	public void testUserOracleAddUserInDatabase() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			assertSame(userService.addUser("test", "test", 0).get().getClass(), User.class);
			
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			fail(e.toString());
		} finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testUserOracleAddUserInDatabaseAlreadyExist() {
		expectedException.expect(NoSuchElementException.class);
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			userService.addUser("test", "test", 0);
			userService.addUser("test", "ee", 0).get();	
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative test
		}
		finally {
	
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testUserOracleCheckUsernameThatExists() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
		
			String sql = "insert into p0_users (username, passcode, adminaccess) values('test', 'test', 0)";

			try (PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();	
				assertTrue(userService.checkUser("test"));
			}
		} catch (SQLException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testUserOracleCheckUsernameNotExisting() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);		
			assertFalse(userService.checkUser("test"));
		} catch (SQLException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testUserOracleLoginExistingUser() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "insert into p0_users (username, passcode, adminaccess) values('test', 'test', 0)";
			
			try (PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();			
				userService.loginUser("test", "test").get();
			}		
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testUserOracleLoginNonExistingUser() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
				
			userService.loginUser("test", "test").get();
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative test
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testUserOracleLoginWrongPassword() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
				
			userService.loginUser("test", "hello").get();
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative test
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testRemoveUserNonExisting() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
		
			userService.removeUser(0).get();
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative test
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testRemoveUserExistingWithBalance() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "call createuser('test', 'test', 0, ?)";
			
			try (CallableStatement stmt = DBConnection.get().getConnection().prepareCall(sql)) {
				stmt.registerOutParameter(1, Types.INTEGER);
				stmt.execute();		
				
				int userid = stmt.getInt(1);
				
				String sql2 = "insert into p0_checking values(?, ?, ?)";
				try (PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.setInt(1, userid);
					stmt2.setInt(2,100000);
					stmt2.setDouble(3, 10.0);
					stmt2.execute();
					
					UserService.get().removeUser(userid).get();
				}
			}		
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative test
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testRemoveUserExistingWithNoBalance() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "call createuser('test', 'test', 0, ?)";
			
			try (CallableStatement stmt = DBConnection.get().getConnection().prepareCall(sql)) {
				stmt.registerOutParameter(1, Types.INTEGER);
				stmt.execute();		
				
				int userid = stmt.getInt(1);
				
				String sql2 = "insert into p0_checking values(?, ?, ?)";
				try (PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.setInt(1, userid);
					stmt2.setInt(2,100000);
					stmt2.setDouble(3, 0.0);
					stmt2.execute();
					User user = UserService.get().removeUser(userid).get();
					assertSame(user.getClass(), User.class);
				}
			}		
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testRemoveUserNonExistingAsAdmin() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			userService.removeUserAsAdmin(100000);
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			//Negative
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testRemoveUserExistingAsAdmin() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "call createuser('test', 'test', 0, ?)";
			
			try (CallableStatement stmt = DBConnection.get().getConnection().prepareCall(sql)) {
				stmt.registerOutParameter(1, Types.INTEGER);
				stmt.execute();		
				
				int userid = stmt.getInt(1);
				
				String sql2 = "insert into p0_checking values(?, ?, ?)";
				try (PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.setInt(1, userid);
					stmt2.setInt(2,100000);
					stmt2.setDouble(3, 10.0);
					stmt2.execute();
					Users users = UserService.get().removeUserAsAdmin(userid).get();
					assertSame(users.getClass(), Users.class);
				}
			}		
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testRetrieveUsersAsAdminNoUsers() {
		DBConnection.get().connect();	
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			userService.retrieveUsersAsAdmin().get();
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			//Negative
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Testing AccountChecking
	 */

	@Test
	public void testCreateCheckingAccount() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				AccountInformation account = accountService.createAccount(100000).get();
				assertSame(account.getClass(), AccountInformation.class);
			}
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testCreateCheckingAccountNonExistingUser() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
	
			accountService.createAccount(100000).get();

		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testRetrieveAccounts() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 0.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					AccountChecking account = accountService.retrieveAccounts(100000).get();
					assertSame(account.getClass(), AccountChecking.class);
				}
			}
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			fail(e.toString());
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testRetrieveAccountsNonExistingUser() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
		
			accountService.retrieveAccounts(100000);

		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative test
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testWithdrawExtra() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testWithdrawNegative() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testWithdrawPositive() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			
		}
		finally {
			try {
				DBConnection.get().getConnection().rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
