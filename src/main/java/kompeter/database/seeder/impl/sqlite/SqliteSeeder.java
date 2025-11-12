/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.seeder.impl.sqlite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Logger;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.seeder.ISeeder;
import kompeter.lib.logger.KompeterLogger;

public class SqliteSeeder implements ISeeder {
    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteSeeder.class);

    @Override
    public String getSeederQuery() throws FileNotFoundException, IOException {
        try (InputStream is = ISeeder.class.getResourceAsStream("../seed.sql")) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Override
    public void seed() throws IOException, SQLException {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);

        try (Connection conn = factory.getConnection(); Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery("""
                    SELECT is_seeded FROM db_settings;
                    """);

            if (rs.next() && rs.getBoolean(1)) {
                LOGGER.info(
                        "Database is already seeded. If you added new entries in development, please re-initialize everything.");

                return;
            }
        }

        final String[] queries = Arrays.stream(getSeederQuery().split(";")).filter((str) -> !str.isBlank())
                .map((s) -> s.trim()).toArray(String[]::new);

        try (Connection conn = factory.getConnection(); Statement stmt = conn.createStatement();) {
            conn.setAutoCommit(false);

            for (int i = 0; i < queries.length; ++i) {
                try {
                    stmt.executeUpdate(queries[i]);
                    conn.commit();
                    LOGGER.info(String.format("Seeded with query:\n====\n%s\n====\n", queries[i]));
                } catch (final SQLException err) {
                    try {
                        conn.rollback();
                    } catch (final SQLException err2) {
                        err.addSuppressed(err2);
                    }

                    throw err;
                }
            }
        }
    }
}
