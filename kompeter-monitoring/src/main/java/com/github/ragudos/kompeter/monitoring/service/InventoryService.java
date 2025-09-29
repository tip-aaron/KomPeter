package com.github.ragudos.kompeter.monitoring.service;

import com.github.ragudos.kompeter.database.AbstractMigratorFactory;
import com.github.ragudos.kompeter.database.dao.impl.sqlite.SqliteInventoryDAO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryCountDTO;
import com.github.ragudos.kompeter.database.migrations.Migrator;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.SQLException;
import java.util.List;
import java.sql.Timestamp;

public class InventoryService {

    private static final Logger LOGGER = KompeterLogger.getLogger(InventoryService.class);
    private final SqliteInventoryDAO inventoryDAO = new SqliteInventoryDAO();

    public void printInventoryReport(Timestamp from, Timestamp to) {
        try {
            List<InventoryCountDTO> results = inventoryDAO.getInventoryCount(from, to);

            if (results.isEmpty()) {
                LOGGER.log(Level.WARNING, "No inventory records found for range: {0} → {1}",
                        new Object[]{from, to});
            } else {
                LOGGER.log(Level.INFO, "Fetched {0} inventory records for range: {1} → {2}",
                        new Object[]{results.size(), from, to});
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching inventory report", e);
        }
    }

    public static void main(String[] args) throws IOException {
        InventoryService service = new InventoryService();

        try {
            AbstractMigratorFactory factory
                    = AbstractMigratorFactory.getMigrator(AbstractMigratorFactory.SQLITE);
            Migrator migrator = factory.getMigrator();

            migrator.migrate();

        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Migration failed.";
        }

        Timestamp today = new Timestamp(System.currentTimeMillis());
        Timestamp oneWeekAgo = Timestamp.valueOf(java.time.LocalDateTime.now().minusDays(7));
        service.printInventoryReport(oneWeekAgo, today);

        Files.delete(Paths.get(SqliteFactoryDao.MAIN_DB_FILE_NAME));
    }
}
