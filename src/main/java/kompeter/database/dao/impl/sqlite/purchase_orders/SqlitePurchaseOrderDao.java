/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.impl.sqlite.purchase_orders;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kompeter.database.dao.purchase_orders.PurchaseOrderDao;
import kompeter.database.dto.etc.Supplier;
import kompeter.database.dto.purchases.PurchaseOrder;
import kompeter.database.dto.purchases.PurchaseOrderLine;
import kompeter.database.dto.purchases.PurchaseOrderView;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqlitePurchaseOrderDao implements PurchaseOrderDao {
    @Override
    public int createPurchaseOrder(final Connection conn, final String purchaseCode, final Timestamp purchaseDate,
            final int supplierId, final BigDecimal vatRate) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("create_purchase_order").tableName("purchase_orders").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt("_supplier_id", supplierId);
            stmt.setTimestamp("purchase_date", purchaseDate);
            stmt.setString("purchase_code", purchaseCode);
            stmt.setBigDecimal("vat_rate", vatRate);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public ArrayList<PurchaseOrderView> getAllPurchaseOrderViews(final Connection conn)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance()
                .getQuery(SqlQueryData.builder().fileName("select_all_purchase_orders_view")
                        .tableName("purchase_orders").queryType(SqlQueryType.SELECT).build());

        try (Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery(query);
            final ArrayList<PurchaseOrderView> res = new ArrayList<>();

            while (rs.next()) {
                res.add(PurchaseOrderView.builder()._purchaseOrderId(rs.getInt("_purchase_order_id"))
                        .purchaseCode(rs.getString("purchase_code")).purchaseDate(rs.getTimestamp("purchase_date"))
                        .totalCost(rs.getBigDecimal("total_cost")).totalProducts(rs.getInt("total_products"))
                        .totalQuantity(rs.getInt("total_quantity")).vatRate(rs.getBigDecimal("vat_rate")).build());
            }

            return res;
        }
    }

    @Override
    public ArrayList<PurchaseOrder> getAllPurchaseOrders(final Connection conn) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance()
                .getQuery(SqlQueryData.builder().fileName("select_all_purchase_orders").tableName("purchase_orders")
                        .queryType(SqlQueryType.SELECT).build());

        try (Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery(query);
            final ArrayList<PurchaseOrder> res = new ArrayList<>();

            while (rs.next()) {
                final ObjectMapper mapper = new ObjectMapper();
                final PurchaseOrderLine[] poLines = mapper.readValue(rs.getString("purchase_order_lines"),
                        new TypeReference<PurchaseOrderLine[]>() {
                        });
                final Supplier supplier = Supplier.builder().id(rs.getInt("_supplier_id"))
                        .name(rs.getString("supplier_name")).build();

                res.add(PurchaseOrder.builder().id(rs.getInt("_purchase_order_id")).lines(poLines).supplier(supplier)
                        .vatRate(rs.getBigDecimal("vat_rate")).purchaseCode(rs.getString("purchase_code"))
                        .purchaseDate(rs.getTimestamp("purchase_date")).build());
            }

            return res;
        }
    }
}
