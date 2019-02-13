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
	
	@Test(expected=NoSuchElementException.class)
	public void testWithdrawExtra() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 10.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					accountService.withdraw(100.0, 100000, 100000).get();
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
	
	@Test(expected=NoSuchElementException.class)
	public void testWithdrawNegative() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 10.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					accountService.withdraw(-100.0, 100000, 100000).get();
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
	public void testWithdrawNormal() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 1000.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					AccountChecking accountChecking = accountService.withdraw(100.0, 100000, 100000).get();
					assertEquals(accountChecking.getAccounts().get(0).getBalance(), 900.0);
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
	public void testDepositNegative() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 10.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					accountService.deposit(-100.0, 100000, 100000).get();
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
	public void testDepositNormal() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 1000.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					AccountChecking accountChecking = accountService.deposit(100.0, 100000, 100000).get();
					assertEquals(accountChecking.getAccounts().get(0).getBalance(), 1100.0);
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
	public void testTransferExtra() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql1 = "insert into p0_users values (100001, 'test1', 'test1', 0)";
				
				try(PreparedStatement stmt1 = DBConnection.get().getConnection().prepareStatement(sql1)) {
					stmt1.execute();
					
					String sql2 = "insert into p0_checking values (100000, 100000, 100.0)";
					
					try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
						stmt2.execute();
						
						String sql3 = "insert into p0_checking values(100001, 100001, 0.0)"; 
					
						try(PreparedStatement stmt3 = DBConnection.get().getConnection().prepareStatement(sql3)) {
							stmt3.execute();
							
							accountService.transfer(1000.0, 100000, 100001, 100000).get();
						
						}
					}
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
	
	@Test(expected=NoSuchElementException.class)
	public void testTransferNegative() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql1 = "insert into p0_users values (100001, 'test1', 'test1', 0)";
				
				try(PreparedStatement stmt1 = DBConnection.get().getConnection().prepareStatement(sql1)) {
					stmt1.execute();
					
					String sql2 = "insert into p0_checking values (100000, 100000, 100.0)";
					
					try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
						stmt2.execute();
						
						String sql3 = "insert into p0_checking values(100001, 100001, 0.0)"; 
					
						try(PreparedStatement stmt3 = DBConnection.get().getConnection().prepareStatement(sql3)) {
							stmt3.execute();
							
							accountService.transfer(-100.0, 100000, 100001, 100000).get();
							
						}
					}
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
	public void testTransferNormal() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql1 = "insert into p0_users values (100001, 'test1', 'test1', 0)";
				
				try(PreparedStatement stmt1 = DBConnection.get().getConnection().prepareStatement(sql1)) {
					stmt1.execute();
					
					String sql2 = "insert into p0_checking values (100000, 100000, 100.0)";
					
					try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
						stmt2.execute();
						
						String sql3 = "insert into p0_checking values(100001, 100001, 0.0)"; 
					
						try(PreparedStatement stmt3 = DBConnection.get().getConnection().prepareStatement(sql3)) {
							stmt3.execute();
							
							AccountChecking account = accountService.transfer(100.0, 100000, 100001, 100000).get();
							assertEquals(account.getAccounts().get(0).getBalance(), 0.0);
						}
					}
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
	public void testTransferVoidAccount() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 1000.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					accountService.transfer(100.0, 100000, 100001, 100000).get();
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
	public void testRetrieveAccountsAsAdmin() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 1000.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					AccountChecking account = accountService.retrieveAccountsAsAdmin().get();
					assertSame(account.getClass(), AccountChecking.class);
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
	
	@Test(expected=NoSuchElementException.class)
	public void testDeleteAccountWithBalance() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 1000.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					accountService.deleteAccount(100000, 100000).get();
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
	public void testDeleteAccountWithNoBalance() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 0.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					AccountChecking account = accountService.deleteAccount(100000, 100000).get();
					assertEquals(account.getAccounts().size(), 0);
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
	
	@Test
	public void testDeleteAccountAsAdmin() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
			String sql = "insert into p0_users values (100000, 'test', 'test', 0)";
			
			try(PreparedStatement stmt = DBConnection.get().getConnection().prepareStatement(sql)) {
				stmt.execute();
				
				String sql2 = "insert into p0_checking values(100000, 100000, 0.0)"; 
				
				try(PreparedStatement stmt2 = DBConnection.get().getConnection().prepareStatement(sql2)) {
					stmt2.execute();
					
					AccountChecking account = accountService.deleteAccountAsAdmin(100000).get();
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
	public void testDeleteFalseAccountAsAdmin() {
		DBConnection.get().connect();
		try {
			DBConnection.get().getConnection().setAutoCommit(false);
				accountService.deleteAccountAsAdmin(10001).get();
		} catch (SQLException e) {
			fail(e.toString());
		} catch (NoSuchElementException e) {
			// Negative check
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
