/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.impl.sqlite.product;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import kompeter.database.dao.products.ProductDao;
import kompeter.database.dto.etc.AverageCost;
import kompeter.database.dto.products.CartProduct;
import kompeter.database.dto.products.Product;
import kompeter.database.dto.products.ProductBrand;
import kompeter.database.dto.products.ProductCategory;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteProductDao implements ProductDao {
    @Override
    public void addQuantity(final Connection conn, final int productId, final int toAdd)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("add_quantity")
                .tableName("products").queryType(SqlQueryType.UPDATE).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query)) {
            stmt.setInt("_product_id", productId);
            stmt.setInt("quantity_to_add", toAdd);

            stmt.executeUpdate();
        }
    }

    @Override
    public void changeSellingPrice(final Connection conn, final int productId, final BigDecimal newPrice,
            final BigDecimal avgCost, final BigDecimal avgCostVatRate) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("change_selling_price").tableName("products").queryType(SqlQueryType.UPDATE).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query)) {
            stmt.setBigDecimal("net_price", newPrice);
            stmt.setBigDecimal("average_cost", avgCost);
            stmt.setBigDecimal("average_cost_vat_rate", avgCostVatRate);
            stmt.setInt("_product_id", productId);

            stmt.executeUpdate();
        }
    }

    @Override
    public int createProduct(final Connection conn, final Product product) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_product")
                .tableName("products").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt("_product_category_id", product.getCategory().getId());
            stmt.setInt("_product_brand_id", product.getBrand().getId());
            stmt.setString("name", product.getName());
            stmt.setString("description", product.getDescription());
            stmt.setString("display_image", product.getDisplayImage());
            stmt.setBigDecimal("markup_rate", product.getMarkupRate());
            stmt.setBigDecimal("net_price", product.getNetPrice());
            stmt.setBigDecimal("average_cost", product.getAvgCost());
            stmt.setBigDecimal("average_cost_vat_rate", product.getAvgCostVatRate());
            stmt.setInt("minimum_quantity", product.getMinimumQuantity());
            stmt.setInt("quantity_in_hand", product.getQuantityInHand());
            stmt.setBoolean("is_active", product.isActive());
            stmt.setBoolean("is_deleted", product.isDeleted());

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public void decQuantity(final Connection conn, final int productId, final int toDec)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("dec_quantity")
                .tableName("products").queryType(SqlQueryType.UPDATE).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query)) {
            stmt.setInt("_product_id", productId);
            stmt.setInt("quantity_to_dec", toDec);

            stmt.executeUpdate();
        }
    }

    @Override
    public boolean exists(final Connection conn, final String name) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_exists_by_name").tableName("products").queryType(SqlQueryType.SELECT).build());

        try (final PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }

    @Override
    public boolean exists(final Connection conn, final int productId) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_exists_by_id").tableName("products").queryType(SqlQueryType.SELECT).build());

        try (final PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }

    @Override
    public ArrayList<CartProduct> getAllCartProducts(final Connection conn) throws IOException, SQLException {
        try (final Statement stmt = conn.createStatement()) {
            final ArrayList<CartProduct> products = new ArrayList<>();

            final ResultSet rs = stmt.executeQuery(
                    SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("select_all_cart_products")
                            .tableName("products").queryType(SqlQueryType.SELECT).build()));

            while (rs.next()) {
                final String name = rs.getString("name");

                final CartProduct product = CartProduct.builder().id(rs.getInt("_product_id")).name(name)
                        .displayImage(rs.getString("display_image")).netPrice(rs.getBigDecimal("net_price"))
                        .quantityInHand(rs.getInt("quantity_in_hand")).build();

                products.add(product);
            }

            return products;
        }
    }

    @Override
    public ArrayList<Product> getAllProducts(final Connection conn) throws SQLException, IOException {
        try (final Statement stmt = conn.createStatement()) {
            final ArrayList<Product> products = new ArrayList<>();

            final ResultSet rs = stmt.executeQuery(SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                    .fileName("select_all_products").tableName("products").queryType(SqlQueryType.SELECT).build()));

            while (rs.next()) {
                final String name = rs.getString("name");
                final String categoryName = rs.getString("category_name");
                final String brandName = rs.getString("brand_name");
                final ProductCategory productCategory = ProductCategory.builder().name(categoryName)
                        .id(rs.getInt("_product_category_id")).build();
                final ProductBrand productBrand = ProductBrand.builder().name(brandName)
                        .id(rs.getInt("_product_brand_id")).build();
                final Product product = Product.builder().id(rs.getInt("_product_id")).category(productCategory)
                        .brand(productBrand).name(name).description(rs.getString("description"))
                        .displayImage(rs.getString("display_image")).markupRate(rs.getBigDecimal("markup_rate"))
                        .netPrice(rs.getBigDecimal("net_price")).avgCost(rs.getBigDecimal("average_cost"))
                        .avgCostVatRate(rs.getBigDecimal("average_cost_vat_rate"))
                        .minimumQuantity(rs.getInt("minimum_quantity")).quantityInHand(rs.getInt("quantity_in_hand"))
                        .isActive(rs.getBoolean("is_active")).isDeleted(rs.getBoolean("is_deleted")).build();

                products.add(product);
            }

            return products;
        }
    }

    @Override
    public Optional<AverageCost> getAvgCost(final Connection conn, final int productId)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_average_cost").tableName("products").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);

            final ResultSet rs = stmt.executeQuery();

            return rs.next()
                    ? Optional.of(AverageCost.builder().avgCost(rs.getBigDecimal("average_cost"))
                            .avgVatRate(rs.getBigDecimal("average_cost_vat_rate")).build())
                    : Optional.empty();
        }
    }

    @Override
    public Optional<BigDecimal> getMarkupRate(final Connection conn, final int productId)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_markup_rate").tableName("products").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() ? Optional.of(rs.getBigDecimal("markup_rate")) : Optional.empty();
        }
    }
}
