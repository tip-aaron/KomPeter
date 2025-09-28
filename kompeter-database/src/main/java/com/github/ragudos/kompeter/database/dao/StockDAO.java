/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao;

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
public interface StockDAO {
    public List<PurchaseUnitDTO> getPurchaseUnit();
    public List<PurchaseUnitDTO> getPurchaseUnit(LocalDateTime from);
    public List<PurchaseUnitDTO> getPurchaseUnit(LocalDateTime from, LocalDateTime to);
    
    public List<SalesUnitDTO> getSalesUnit();
    public List<SalesUnitDTO> getSalesUnit(LocalDateTime from);
    public List<SalesUnitDTO> getSalesUnit(LocalDateTime from, LocalDateTime to);
    
    public List<OnHandUnitDTO> getOnHandUnit();
    public List<OnHandUnitDTO> getOnHandUnit(LocalDateTime from);
    public List<OnHandUnitDTO> getOnHandUnit(LocalDateTime from, LocalDateTime to);
    
    public List<LowStockItemsDTO> getLowStockItems();
    public List<LowStockItemsDTO> getLowStockItems(LocalDateTime from);
    public List<LowStockItemsDTO> getLowStockItems(LocalDateTime from, LocalDateTime to);
    
    public List<OldItemsDTO> getOldItems();
    public List<OldItemsDTO> getOldItems(LocalDateTime from);
    public List<OldItemsDTO> getOldItems(LocalDateTime from, LocalDateTime to);
}
