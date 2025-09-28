package com.github.ragudos.kompeter.database;

import com.github.ragudos.kompeter.database.DBConnection;
import com.github.ragudos.kompeter.database.DBSetup;

public class Main {
	public static void main(String[] args) {
		new DBSetup(DBConnection.getConnection());
	}
}
