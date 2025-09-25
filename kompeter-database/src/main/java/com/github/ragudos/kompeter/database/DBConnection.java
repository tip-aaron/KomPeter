package com.github.ragudos.kompeter.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private final static String path = "src/main/resources/com/github/ragudos/kompeter/database/db/";
	public static final String URL = "jdbc:sqlite:" + path + "kompeter.db";
	
	private DBConnection() {}
	
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL);
		}catch(SQLException e) {
			System.err.println("Connection failed: " + e.getMessage());
			return null;
		}
		
	}
	
	

}
