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
public class InventoryValueDTO {
    public Timestamp date;
    public float totalInventoryValue;
    public float totalPurchaseValue;
    public float totalSalesValue;

    public InventoryValueDTO(Timestamp date, float totalInventoryValue, float totalPurchaseValue, float totalSalesValue) {
        this.date = date;
        this.totalInventoryValue = totalInventoryValue;
        this.totalPurchaseValue = totalPurchaseValue;
        this.totalSalesValue = totalSalesValue;
    }

    public InventoryValueDTO(Timestamp date, float totalInventoryValue) {
        this.date = date;
        this.totalInventoryValue = totalInventoryValue;
    }

    public Timestamp getDate() {
        return date;
    }

    public float getTotalInventoryValue() {
        return totalInventoryValue;
    }

    public float getTotalPurchaseValue() {
        return totalPurchaseValue;
    }

    public float getTotalSalesValue() {
        return totalSalesValue;
    }

    @Override
    public String toString() {
        return "InventoryValueDTO{" + "date=" + date + ", totalInventoryValue=" + totalInventoryValue + ", totalPurchaseValue=" + totalPurchaseValue + ", totalSalesValue=" + totalSalesValue + '}';
    }
    
    
}
