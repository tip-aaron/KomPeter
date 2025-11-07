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

public interface SaleLineDao {
    int createSaleLine(Connection conn, int saleId, int productId, BigDecimal netPrice, int quantity)
            throws IOException, SQLException;
}
