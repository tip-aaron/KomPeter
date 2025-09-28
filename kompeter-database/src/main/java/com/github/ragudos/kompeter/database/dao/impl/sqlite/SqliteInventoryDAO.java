/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao.impl.sqlite;

import com.github.ragudos.kompeter.database.dao.InventoryDAO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryCountDTO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryValueDTO;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author Hanz Mapua
 */
public class SqliteInventoryDAO implements InventoryDAO {
    @Override
    public List<InventoryCountDTO> getInventoryCount() {
    
        return null;
    
    }
    @Override
    public List<InventoryCountDTO> getInventoryCount(LocalDateTime from) {
    
        return null;
    
    }
    @Override
    public List<InventoryCountDTO> getInventoryCount(LocalDateTime from, LocalDateTime to) {
    
        return null;
    
    }
    
    
    @Override
    public List<InventoryValueDTO> getInventoryValue() {
    
        return null;
    
    }
    @Override
    public List<InventoryValueDTO> getInventoryValue(LocalDateTime from) {
    
        return null;
    
    }
    @Override
    public List<InventoryValueDTO> getInventoryValue(LocalDateTime from, LocalDateTime to) {
    
        return null;
    
    }
}
