/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.ragudos.kompeter.database.dao;

import com.github.ragudos.kompeter.database.dto.SalesDTOs.ExpensesDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.ProfitDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.RevenueDTO;
import com.github.ragudos.kompeter.database.dto.SalesDTOs.TopSellingDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Hanz Mapua
 */
public interface SalesDAO {
    public List<RevenueDTO> getRevenue();
    public List<RevenueDTO> getRevenue(LocalDateTime from);
    public List<RevenueDTO> getRevenue(LocalDateTime from, LocalDateTime to);
    
    public List<ExpensesDTO> getExpenses();
    public List<ExpensesDTO> getExpenses(LocalDateTime from);
    public List<ExpensesDTO> getExpenses(LocalDateTime from, LocalDateTime to);
    
    public List<ProfitDTO> getProfit();
    public List<ProfitDTO> getProfit(LocalDateTime from);
    public List<ProfitDTO> getProfit(LocalDateTime from, LocalDateTime to);

    public List<TopSellingDTO> getTopSellingItems();
    public List<TopSellingDTO> getTopSellingItems(LocalDateTime from);
    public List<TopSellingDTO> getTopSellingItems(LocalDateTime from, LocalDateTime to);
}
