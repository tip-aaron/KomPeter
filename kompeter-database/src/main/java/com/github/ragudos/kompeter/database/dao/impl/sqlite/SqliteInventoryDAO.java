/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao.impl.sqlite;

import com.github.ragudos.kompeter.database.dao.InventoryDAO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryCountDTO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryValueDTO;
import java.sql.Timestamp;
import java.util.List;
/**
 *
 * @author Hanz Mapua
 */
public class SqliteInventoryDAO implements InventoryDAO {
    @Override
    public List<InventoryCountDTO> getInventoryCount() {
        return getInventoryCount(null, null);
    }
    @Override
    public List<InventoryCountDTO> getInventoryCount(Timestamp from) {
        return getInventoryCount(from, null);
    }
    @Override
    public List<InventoryCountDTO> getInventoryCount(Timestamp from, Timestamp to) {
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
    
    
    @Override
    public List<InventoryValueDTO> getInventoryValue() {
        return getInventoryValue(null, null);
    }
    @Override
    public List<InventoryValueDTO> getInventoryValue(Timestamp from) {
        return getInventoryValue(from, null);
    }
    @Override
    public List<InventoryValueDTO> getInventoryValue(Timestamp from, Timestamp to) {
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
