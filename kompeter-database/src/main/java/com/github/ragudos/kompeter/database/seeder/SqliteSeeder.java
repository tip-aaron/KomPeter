package com.github.ragudos.kompeter.database.seeder;

import java.util.logging.Logger;
import java.sql.SQLException;

import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.database.sqlite.migrations.SqliteMigratorFactory;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;

public class SqliteSeeder implements Seeder {
	private static final Logger LOGGER = KompeterLogger.getLogger(SqliteSeeder.class);
	@Override
	public void seed() throws SQLException {
		LOGGER.info("Seeding database...");
		
		var query = SqliteMigratorFactory.getSeederQuery();
		if (query == null) {
			LOGGER.warning("Seeder query not found. Stopping...");
			return;
		}
		
		try(var conn = SqliteFactoryDao.createConnection();) {
			conn.setAutoCommit(false);
			
			try(var stmnt = conn.createStatement()) {
				var queries = query.split(";");
				
				LOGGER.info("Starting to seed for " + queries.length + " operations...");
				
				for (int i = 0; i < queries.length; ++i) {
					var q = queries[i].trim();
					LOGGER.info("[" + (i + 1) + "/" + queries.length + "] " + q);
					
					try {
						stmnt.execute(q);
						conn.commit();
						LOGGER.info("success!");
					} catch (SQLException e) {
						LOGGER.warning("fail! " + e.getMessage());
						conn.rollback();
					}
				}
			}
		}
		
	}

}
