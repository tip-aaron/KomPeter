/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao.impl.sqlite;

import com.github.ragudos.kompeter.database.dao.InventoryDAO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryCountDTO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryValueDTO;
import com.github.ragudos.kompeter.database.sqlite.SqliteFactoryDao;
import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hanz Mapua
 */
public class SqliteInventoryDAO implements InventoryDAO {

    @Override
    public List<InventoryCountDTO> getInventoryCount() throws SQLException {
        return getInventoryCount(null, null);
    }

    @Override
    public List<InventoryCountDTO> getInventoryCount(Timestamp from) throws SQLException {
        return getInventoryCount(from, null);
    }

    @Override
    public List<InventoryCountDTO> getInventoryCount(Timestamp from, Timestamp to) throws SQLException {
        List<InventoryCountDTO> results = new ArrayList<>();

        String sqlFile;
        if (from == null && to == null) {
            sqlFile = "inventory_count_all.sql";
        } else if (from == null) {
            sqlFile = "inventory_count_to.sql";
        } else {
            sqlFile = "inventory_count_range.sql";
        }

        String resourcePath = "/com/github/ragudos/kompeter/database/sql/sqlite/select/InventorySQLs/" + sqlFile;
        String query;

        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new SQLException("SQL resource not found on classpath: " + resourcePath);
            }
            query = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SQLException("Error reading SQL resource: " + resourcePath, e);
        }

        try (Connection conn = SqliteFactoryDao.createConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (from != null) {
                stmt.setTimestamp(paramIndex++, from);
            }
            if (to != null) {
                stmt.setTimestamp(paramIndex, to);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    InventoryCountDTO dto = new InventoryCountDTO(
                            rs.getTimestamp("date"),
                            rs.getInt("total_inventory"),
                            rs.getInt("total_purchased"),
                            rs.getInt("total_sold")
                    );
                    results.add(KompeterLogger.log(dto));
                }
            }
        }

        return results;
    }

    @Override
    public List<InventoryValueDTO> getInventoryValue() throws SQLException {
        return getInventoryValue(null, null);
    }

    @Override
    public List<InventoryValueDTO> getInventoryValue(Timestamp from) throws SQLException {
        return getInventoryValue(from, null);
    }

    @Override
    public List<InventoryValueDTO> getInventoryValue(Timestamp from, Timestamp to) throws SQLException {
        String query;

        if (from == null && to == null) {
            query = "";
        } else if (from != null && to == null) {
            query = "";
        } else {
            query = "";
        }

        return null;
    }
}
