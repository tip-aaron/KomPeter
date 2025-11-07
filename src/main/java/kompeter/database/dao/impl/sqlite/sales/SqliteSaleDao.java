/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.impl.sqlite.sales;

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

import kompeter.database.dao.sales.SaleDao;
import kompeter.database.dto.etc.Customer;
import kompeter.database.dto.sales.Sale;
import kompeter.database.dto.sales.SaleDiscount;
import kompeter.database.dto.sales.SaleLine;
import kompeter.database.dto.sales.SaleView;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteSaleDao implements SaleDao {
    @Override
    public int createSale(final Connection conn, final int customerId, final Timestamp saleDate, final String saleCode,
            final BigDecimal vatRate) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_sale")
                .tableName("sales").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt("_customer_id", customerId);
            stmt.setTimestamp("sale_date", saleDate);
            stmt.setString("sale_code", saleCode);
            stmt.setBigDecimal("vat_rate", vatRate);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public ArrayList<SaleView> getAllSaleViews(final Connection conn) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_all_sales_view").tableName("sales").queryType(SqlQueryType.SELECT).build());

        try (Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery(query);
            final ArrayList<SaleView> res = new ArrayList<>();

            while (rs.next()) {
                res.add(SaleView.builder().saleId(rs.getInt("_sale_id")).saleCode(rs.getString("sale_code"))
                        .saleDate(rs.getTimestamp("sale_date")).vatRate(rs.getBigDecimal("vat_rate"))
                        .customerName(rs.getString("customer_name")).totalDiscount(rs.getBigDecimal("total_discount"))
                        .totalNetPrice(rs.getBigDecimal("total_net_price"))
                        .grossTotalNetPrice(rs.getBigDecimal("gross_total_net_price"))
                        .totalProducts(rs.getInt("total_products")).totalQuantity(rs.getInt("total_quantity")).build());
            }

            return res;
        }
    }

    @Override
    public ArrayList<Sale> getAllSales(final Connection conn) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_all_sales").tableName("sales").queryType(SqlQueryType.SELECT).build());

        try (Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery(query);

            final ArrayList<Sale> res = new ArrayList<>();

            while (rs.next()) {
                final ObjectMapper mapper = new ObjectMapper();
                final SaleLine[] saleLines = mapper.readValue(rs.getString("sale_lines"),
                        new TypeReference<SaleLine[]>() {
                        });
                final SaleDiscount[] saleDiscounts = mapper.readValue(rs.getString("sale_discounts"),
                        new TypeReference<SaleDiscount[]>() {
                        });

                final Customer customer = Customer.builder().id(rs.getInt("_customer_id"))
                        .name(rs.getString("customer_name")).build();

                res.add(Sale.builder().id(rs.getInt("_sale_id")).saleLines(saleLines).customer(customer)
                        .vatRate(rs.getBigDecimal("vat_rate")).saleCode(rs.getString("sale_code"))
                        .saleDate(rs.getTimestamp("sale_date")).saleDiscounts(saleDiscounts).build());
            }

            return res;
        }
    }
}
