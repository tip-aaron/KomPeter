/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao.impl.sqlite;

import com.github.ragudos.kompeter.database.dao.SalesDAO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.ExpensesDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.ProfitDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.RevenueDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.TopSellingDTO;
import java.sql.Timestamp;
import java.util.List;


public class SqliteSalesDAO implements SalesDAO {
    @Override
    public List<RevenueDTO> getRevenue() {
        return null;
    }
    @Override
    public List<RevenueDTO> getRevenue(Timestamp from) {
        return null;
    }
    @Override
    public List<RevenueDTO> getRevenue(Timestamp from, Timestamp to) {
        return null;
    }
    
    
    @Override
    public List<ExpensesDTO> getExpenses() {
        return null;
    }
    @Override
    public List<ExpensesDTO> getExpenses(Timestamp from) {
        return null;
    }
    @Override
    public List<ExpensesDTO> getExpenses(Timestamp from, Timestamp to) {
        return null;
    }
    
    
    @Override
    public List<ProfitDTO> getProfit() {
        return null;
    }
    @Override
    public List<ProfitDTO> getProfit(Timestamp from) {
        return null;
    }
    @Override
    public List<ProfitDTO> getProfit(Timestamp from, Timestamp to) {
        return null;
    }
    
    
    @Override
    public List<TopSellingDTO> getTopSellingItems() {
        return null;
    }
    @Override
    public List<TopSellingDTO> getTopSellingItems(Timestamp from) {
        return null;
    }
    @Override
    public List<TopSellingDTO> getTopSellingItems(Timestamp from, Timestamp to) {
        return null;
    }
}
