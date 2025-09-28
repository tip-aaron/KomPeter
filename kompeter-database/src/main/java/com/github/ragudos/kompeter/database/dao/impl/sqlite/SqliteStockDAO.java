/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao.impl.sqlite;

import com.github.ragudos.kompeter.database.dao.StockDAO;
import com.github.ragudos.kompeter.database.dto.StockDTOs.LowStockItemsDTO;
import com.github.ragudos.kompeter.database.dto.StockDTOs.OldItemsDTO;
import com.github.ragudos.kompeter.database.dto.StockDTOs.OnHandUnitDTO;
import com.github.ragudos.kompeter.database.dto.StockDTOs.PurchaseUnitDTO;
import com.github.ragudos.kompeter.database.dto.StockDTOs.SalesUnitDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Hanz Mapua
 */
public class SqliteStockDAO implements StockDAO {
    @Override
    public List<PurchaseUnitDTO> getPurchaseUnit() {
        return null;
    }
    @Override
    public List<PurchaseUnitDTO> getPurchaseUnit(LocalDateTime from) {
        return null;
    }
    @Override
    public List<PurchaseUnitDTO> getPurchaseUnit(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<SalesUnitDTO> getSalesUnit() {
        return null;
    }
    @Override
    public List<SalesUnitDTO> getSalesUnit(LocalDateTime from) {
        return null;
    }
    @Override
    public List<SalesUnitDTO> getSalesUnit(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<OnHandUnitDTO> getOnHandUnit() {
        return null;
    }
    @Override
    public List<OnHandUnitDTO> getOnHandUnit(LocalDateTime from) {
        return null;
    }
    @Override
    public List<OnHandUnitDTO> getOnHandUnit(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<LowStockItemsDTO> getLowStockItems() {
        return null;
    }
    @Override
    public List<LowStockItemsDTO> getLowStockItems(LocalDateTime from) {
        return null;
    }
    @Override
    public List<LowStockItemsDTO> getLowStockItems(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<OldItemsDTO> getOldItems() {
        return null;
    }
    @Override
    public List<OldItemsDTO> getOldItems(LocalDateTime from) {
        return null;
    }
    @Override
    public List<OldItemsDTO> getOldItems(LocalDateTime from, LocalDateTime to) {
        return null;
    }
}
