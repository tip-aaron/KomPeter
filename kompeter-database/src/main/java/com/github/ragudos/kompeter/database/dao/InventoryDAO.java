/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryCountDTO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryValueDTO;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Hanz Mapua
 */
public interface InventoryDAO {
    public List<InventoryCountDTO> getInventoryCount() throws SQLException;
    public List<InventoryCountDTO> getInventoryCount(Timestamp from) throws SQLException;
    public List<InventoryCountDTO> getInventoryCount(Timestamp from, Timestamp to) throws SQLException;
    
    public List<InventoryValueDTO> getInventoryValue() throws SQLException;
    public List<InventoryValueDTO> getInventoryValue(Timestamp from) throws SQLException;
    public List<InventoryValueDTO> getInventoryValue(Timestamp from, Timestamp to) throws SQLException;
}
