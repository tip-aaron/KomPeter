/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.loader.sqlite;

import kompeter.database.loader.AQueryLoader;

public class SqliteQueryLoader extends AQueryLoader {
    static SqliteQueryLoader instance;

    public static synchronized SqliteQueryLoader getInstance() {
        if (instance == null) {
            return new SqliteQueryLoader();
        }

        return instance;
    }

    private SqliteQueryLoader() {
    }

    @Override
    public String getDatabaseName() {
        return "sqlite";
    }
}
