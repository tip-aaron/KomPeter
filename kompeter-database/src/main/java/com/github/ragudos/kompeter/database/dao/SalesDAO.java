/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.SalesDTOs.ExpensesDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.ProfitDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.RevenueDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.TopSellingDTO;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Hanz Mapua
 */
public interface SalesDAO {
    public List<RevenueDTO> getRevenue();
    public List<RevenueDTO> getRevenue(Timestamp from);
    public List<RevenueDTO> getRevenue(Timestamp from, Timestamp to);
    
    public List<ExpensesDTO> getExpenses();
    public List<ExpensesDTO> getExpenses(Timestamp from);
    public List<ExpensesDTO> getExpenses(Timestamp from, Timestamp to);
    
    public List<ProfitDTO> getProfit();
    public List<ProfitDTO> getProfit(Timestamp from);
    public List<ProfitDTO> getProfit(Timestamp from, Timestamp to);

    public List<TopSellingDTO> getTopSellingItems();
    public List<TopSellingDTO> getTopSellingItems(Timestamp from);
    public List<TopSellingDTO> getTopSellingItems(Timestamp from, Timestamp to);
}
