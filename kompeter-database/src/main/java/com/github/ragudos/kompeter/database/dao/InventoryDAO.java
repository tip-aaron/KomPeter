/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryCountDTO;
import com.github.ragudos.kompeter.database.dto.InventoryDTOs.InventoryValueDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Hanz Mapua
 */
public interface InventoryDAO {
    public List<InventoryCountDTO> getInventoryCount();
    public List<InventoryCountDTO> getInventoryCount(LocalDateTime from);
    public List<InventoryCountDTO> getInventoryCount(LocalDateTime from, LocalDateTime to);
    
    public List<InventoryValueDTO> getInventoryValue();
    public List<InventoryValueDTO> getInventoryValue(LocalDateTime from);
    public List<InventoryValueDTO> getInventoryValue(LocalDateTime from, LocalDateTime to);
}
