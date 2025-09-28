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
import java.time.LocalDateTime;
import java.util.List;


public class SqliteSalesDAO implements SalesDAO {
    @Override
    public List<RevenueDTO> getRevenue() {
        return null;
    }
    @Override
    public List<RevenueDTO> getRevenue(LocalDateTime from) {
        return null;
    }
    @Override
    public List<RevenueDTO> getRevenue(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<ExpensesDTO> getExpenses() {
        return null;
    }
    @Override
    public List<ExpensesDTO> getExpenses(LocalDateTime from) {
        return null;
    }
    @Override
    public List<ExpensesDTO> getExpenses(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<ProfitDTO> getProfit() {
        return null;
    }
    @Override
    public List<ProfitDTO> getProfit(LocalDateTime from) {
        return null;
    }
    @Override
    public List<ProfitDTO> getProfit(LocalDateTime from, LocalDateTime to) {
        return null;
    }
    
    
    @Override
    public List<TopSellingDTO> getTopSellingItems() {
        return null;
    }
    @Override
    public List<TopSellingDTO> getTopSellingItems(LocalDateTime from) {
        return null;
    }
    @Override
    public List<TopSellingDTO> getTopSellingItems(LocalDateTime from, LocalDateTime to) {
        return null;
    }
}
