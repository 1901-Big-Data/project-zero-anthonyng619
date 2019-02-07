package com.revature.anthony.nguyen.project0.project0;

import java.util.Scanner;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Initializing the Bank of Revature.");
        System.out.println("Establishing connection... Please wait.");
        
        UserService dbConn = UserService.get();
        if(!dbConn.connect()) {
        	System.out.println("Connection to the bank's database failed.");
        	return;
        }
        System.out.println("\n\n\n\n\n\n");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Welcome to the Bank of Revature!");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        ConsoleDisplay disp = new ConsoleDisplay();
        
        disp.start();
    }
}
