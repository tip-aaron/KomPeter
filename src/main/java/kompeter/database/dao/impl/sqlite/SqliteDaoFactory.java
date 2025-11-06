/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.impl.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import kompeter.constants.Directories;
import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.impl.sqlite.product.SqliteProductDao;
import kompeter.database.dao.impl.sqlite.users.SqliteAccountDao;
import kompeter.database.dao.impl.sqlite.users.SqliteSessionDao;
import kompeter.database.dao.impl.sqlite.users.SqliteUserDao;
import kompeter.database.dao.products.ProductDao;
import kompeter.database.dao.users.AccountDao;
import kompeter.database.dao.users.SessionDao;
import kompeter.database.dao.users.UserDao;

public class SqliteDaoFactory extends ADaoFactory {
    private static SqliteDaoFactory instance;

    static final String MAIN_DB_URL;

    static {
        MAIN_DB_URL = String.format("jdbc:sqlite:/%s%smain", Directories.SQLITE, File.separator);
    }

    public static synchronized SqliteDaoFactory getInstance() {
        if (instance == null) {
            instance = new SqliteDaoFactory();
        }

        return instance;
    }

    @Override
    protected Connection createConnection() throws SQLException {
        try {
            Class.forName("java.sql.Driver");
        } catch (final ClassNotFoundException err) {
            throw new RuntimeException("SQLite JDBC Driver not found", err);
        }

        return DriverManager.getConnection(MAIN_DB_URL);
    }

    @Override
    public AccountDao getAccountDao() {
        return new SqliteAccountDao();
    }

    @Override
    public ProductDao getProductDao() {
        return new SqliteProductDao();
    }

    @Override
    public SessionDao getSessionDao() {
        return new SqliteSessionDao();
    }

    @Override
    public UserDao getUserDao() {
        return new SqliteUserDao();
    }
}
