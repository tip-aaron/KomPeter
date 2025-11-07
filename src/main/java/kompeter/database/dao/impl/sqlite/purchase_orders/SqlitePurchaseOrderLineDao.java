package kompeter.database.dao.impl.sqlite.purchase_orders;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kompeter.database.dao.purchase_orders.PurchaseOrderLineDao;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqlitePurchaseOrderLineDao implements PurchaseOrderLineDao {
    @Override
    public int createPurchaseOrderLine(final Connection conn, final int productId, final int purchaseOrderId,
            final int quantity,
            final BigDecimal unitPrice) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance()
                .getQuery(SqlQueryData.builder().fileName("create_purchase_order_line")
                        .tableName("purchase_order_lines").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt("_product_id", productId);
            stmt.setInt("_purchase_order_ird", purchaseOrderId);
            stmt.setInt("quantity", quantity);
            stmt.setBigDecimal("unit_price", unitPrice);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public int getCountOfPurchaseOrdersOfProduct(final Connection conn, final int productId)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance()
                .getQuery(SqlQueryData.builder().fileName("select_count_of_purchase_orders_of_product")
                        .tableName("purchase_order_lines").queryType(SqlQueryType.INSERT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

}
