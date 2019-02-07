package com.revature.anthony.nguyen.project0.project0.application;

import java.util.Scanner;

import com.revature.anthony.nguyen.project0.project0.service.UserService;
import com.revature.anthony.nguyen.project0.project0.util.DBConnection;

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
       
        if(!DBConnection.get().connect()) {
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
