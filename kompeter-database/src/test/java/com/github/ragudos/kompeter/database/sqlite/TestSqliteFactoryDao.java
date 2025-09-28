package com.github.ragudos.kompeter.database.sqlite;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestSqliteFactoryDao {

    @Test
    @DisplayName("Test migration")
    void testMigration() {
        try {
            AbstractMigratorFactory factory =
                    AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
            Migrator migrator = factory.getMigrator();

            migrator.migrate();

            Files.delete(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Migration failed.";
        }
    }
}
