package kompeter.database.dao.purchase_orders;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import kompeter.database.dto.purchases.PurchaseOrder;
import kompeter.database.dto.purchases.PurchaseOrderView;

public interface PurchaseOrderDao {
    public int createPurchaseOrder(
            Connection conn,
            String purchaseCode,
            Timestamp purchaseDate,
            int supplierId,
            BigDecimal vatRate) throws SQLException, IOException;

    public ArrayList<PurchaseOrder> getAllPurchaseOrders(Connection conn) throws SQLException, IOException;

    public ArrayList<PurchaseOrderView> getAllPurchaseOrderViews(Connection conn) throws SQLException, IOException;
}
