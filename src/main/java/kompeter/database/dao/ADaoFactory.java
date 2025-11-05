/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import kompeter.database.dao.impl.sqlite.SqliteDaoFactory;
import kompeter.lib.logger.KompeterLogger;

public abstract class ADaoFactory {
    static final Logger LOGGER = KompeterLogger.getLogger(ADaoFactory.class);

    public static final int SQLITE = 1;

    public static ADaoFactory getDaoFactory(final int dType) {
        return switch (dType) {
            case SQLITE -> SqliteDaoFactory.getInstance();
            default -> throw new IllegalArgumentException("Unsupported database type");
        };
    }

    public Connection getConnection() throws SQLException {
        return createConnection();
    }

    protected abstract Connection createConnection() throws SQLException;
}
