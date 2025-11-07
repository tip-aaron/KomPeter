/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.sales;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import kompeter.database.dto.sales.Sale;
import kompeter.database.dto.sales.SaleView;

public interface SaleDao {
    int createSale(Connection conn, int customerId, Timestamp saleDate, String saleCode, BigDecimal vatRate)
            throws SQLException, IOException;

    ArrayList<SaleView> getAllSaleViews(Connection conn) throws SQLException, IOException;

    ArrayList<Sale> getAllSales(Connection conn) throws SQLException, IOException;
}
