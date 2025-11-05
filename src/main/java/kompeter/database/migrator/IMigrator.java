/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.migrator;

import java.io.IOException;
import java.sql.SQLException;

public interface IMigrator {
    MigrationQuery[] getMigrationQueries() throws IOException;

    void migrate() throws SQLException, IOException;
}
