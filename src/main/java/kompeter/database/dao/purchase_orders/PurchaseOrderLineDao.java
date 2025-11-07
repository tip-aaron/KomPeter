/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.purchase_orders;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface PurchaseOrderLineDao {
    int createPurchaseOrderLine(Connection conn, int productId, int purchaseOrderId, int quantity, BigDecimal unitPrice)
            throws SQLException, IOException;

    int getCountOfPurchaseOrdersOfProduct(Connection conn, int productId) throws SQLException, IOException;
}
