package com.github.ragudos.kompeter.database.sqlite;

import com.github.ragudos.kompeter.database.AbstractSqlFactoryDao;
import com.github.ragudos.kompeter.utilities.constants.Directories;
import com.github.ragudos.kompeter.utilities.io.FileUtils;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SqliteFactoryDao extends AbstractSqlFactoryDao {
    private static final Logger LOGGER = KompeterLogger.getLogger(SqliteFactoryDao.class);

    public static final String MAIN_DB_FILE_NAME =
            Directories.SQLITE_DIRECTORY + File.separator + "main.db";
    public static final String DB_URL = "jdbc:sqlite:/" + MAIN_DB_FILE_NAME;

    public static final void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    LOGGER.finer("Database connection closed");
                } else {
                    LOGGER.finest("Attempted to close an already closed database connection");
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close database connection", e);
            }
        } else {
            LOGGER.finest("Attempted to close a null database connection");
        }
    }

    public static final Connection createConnection()
            throws SQLException, SQLTimeoutException, RuntimeException {
        FileUtils.createDirectoryIfNotExists(Directories.SQLITE_DIRECTORY);
        FileUtils.createFileIfNotExists(MAIN_DB_FILE_NAME);

        try {
            Class.forName("java.sql.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQLite JDBC Driver not found", e);
            throw new RuntimeException("SQLite JDBC Driver not found", e);
        }

        if (DB_URL == null || DB_URL.isEmpty()) {
            throw new SQLException("Communications link failure: Missing database properties.");
        }

        return DriverManager.getConnection(DB_URL);
    }
}
