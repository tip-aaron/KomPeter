package com.github.ragudos.kompeter.database;

import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.util.logging.Logger;

/**
 * This class is responsible for creating the appropriate SQL factory DAO based on the database
 * type. It uses a factory method pattern to create the appropriate instance. <br>
 * <br>
 * Example usage of the AbstractSqlFactoryDao to retrieve a platform-specific DAO implementation.
 *
 * <p>This snippet demonstrates how to obtain a MySQL-specific factory instance using the factory
 * method {@link AbstractSqlFactoryDao#getSqlFactoryDao(int)}, and then retrieve the {@link
 * SessionDao} to perform a data access operation. This promotes loose coupling between
 * database-specific implementations and the business logic, making the codebase easier to maintain
 * and extend for other database platforms.
 *
 * <pre>
 * // Step 1: Get the factory for the specified database type (MySQL in this
 * // case)
 * var factory = AbstractSqlFactoryDao.getSqlFactoryDao(AbstractSqlFactoryDao.MYSQL);
 *
 * // Step 2: Retrieve the SessionDao implementation from the factory
 * var sessionDao = factory.getSessionDao();
 *
 * // Step 3: Use the Dao to perform operations, such as checking if a session
 * // exists
 * var sessionExists = sessionDao.sessionExists(sessionUid);
 * </pre>
 *
 * <p>This design allows easy substitution of different database backends by changing only the
 * factory input type, without modifying the business logic that relies on the DAO interfaces.
 *
 * @see <a href= "https://www.oracle.com/java/technologies/dataaccessobject.html">Article about DAO
 *     Pattern by Oracle</a>
 */
public abstract class AbstractSqlFactoryDao {
    protected static final Logger LOGGER = KompeterLogger.getLogger(AbstractSqlFactoryDao.class);
    public static final int SQLITE = 1;

    public static AbstractSqlFactoryDao getSqlFactoryDao(int databaseType) {
        return switch (databaseType) {
            case SQLITE -> new SqliteFactoryDao();
            default -> throw new IllegalArgumentException("Unsupported database type: " + databaseType);
        };
    }
}
