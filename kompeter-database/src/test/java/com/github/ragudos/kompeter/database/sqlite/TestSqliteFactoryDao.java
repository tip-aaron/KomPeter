package com.github.ragudos.kompeter.database.sqlite;

import java.sql.Connection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSqliteFactoryDao {
    @Test
    @DisplayName("Test Sqlite database connection")
    void testSqliteDatabaseConnection() {
        try {
            Connection connection = SqliteFactoryDao.createConnection();
            assert connection != null : "Connection should not be null";
            assert !connection.isClosed() : "Connection should be open";
            SqliteFactoryDao.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Failed to connect to SQLite database or create tables.";
        }
    }
}
