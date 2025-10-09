package com.github.ragudos.kompeter.database.sqlite;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.seeder.Seeder;
public class TestSqlFactoryDao {
    @Test
    @DisplayName("Test migration")
    void testMigration() {
        try {
            AbstractMigratorFactory factory =
                    AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
            Migrator migrator = factory.getMigrator();
            Seeder seeder = factory.getSeeder();

            migrator.migrate();
            seeder.seed();

            Files.delete(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
        } catch (Exception e) {
            System.err.println("Error during migration: " + e.getMessage());
            e.printStackTrace(System.err);
            assert false : "Migration failed.";
        }
    }
}
