package com.github.ragudos.kompeter.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.ragudos.kompeter.utilities.io.load.LoadDb;


public class DBSetup {
	private final Connection conn;
	
	public DBSetup(Connection conn) {
		this.conn = conn;
		createDB();
		seedDB();
	}
	
	private void createDB() {
		try (var stmt = conn.createStatement();){
			
			String sql = new LoadDb("kompeter.sql").load();

			for(String s : sql.split(";")) {
				if(!s.trim().isEmpty()) {
					stmt.execute(s);
			}
			}
			System.out.println("Database created successfully");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void seedDB() {
		try(
			var stmt = conn.createStatement();
				){
			String sql = new LoadDb("seed.sql").load();
			for(String s : sql.split(";")) {
				if (!s.trim().isEmpty()) {
					stmt.execute(s);
				}
			}
			
			System.out.println("Database seeded successfully");
			
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("an error occurred: " + e.getMessage());
		}
	}
	
}
