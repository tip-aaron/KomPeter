/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dto.InventoryDTOs;

import java.sql.Timestamp;

/**
 *
 * @author Hanz Mapua
 */
public class InventoryCountDTO {
    public Timestamp date;
    public int totalInventoryCount;
    public int totalPurchaseCount;
    public int totalSalesCount;

    public InventoryCountDTO(Timestamp date, int totalInventoryCount, int totalPurchaseCount, int totalSalesCount) {
        this.date = date;
        this.totalInventoryCount = totalInventoryCount;
        this.totalPurchaseCount = totalPurchaseCount;
        this.totalSalesCount = totalSalesCount;
    }

    public InventoryCountDTO(Timestamp date, int totalInventoryCount) {
        this.date = date;
        this.totalInventoryCount = totalInventoryCount;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getTotalInventoryCount() {
        return totalInventoryCount;
    }

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    public int getTotalSalesCount() {
        return totalSalesCount;
    }

    @Override
    public String toString() {
        return "InventoryCountDTO{" + "date=" + date + ", totalInventoryCount=" + totalInventoryCount + ", totalPurchaseCount=" + totalPurchaseCount + ", totalSalesCount=" + totalSalesCount + '}';
    }
    
    
    
    
}
