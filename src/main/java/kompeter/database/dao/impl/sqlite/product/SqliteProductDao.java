/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.impl.sqlite.product;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import kompeter.database.dao.products.ProductDao;
import kompeter.database.dto.products.CartProduct;
import kompeter.database.dto.products.Product;
import kompeter.database.dto.products.ProductBrand;
import kompeter.database.dto.products.ProductCategory;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.lib.helper.Filter;

public class SqliteProductDao implements ProductDao {
    @Override
    public ArrayList<CartProduct> getAllCartProducts(final Connection conn, final String nameFilter,
            final String[] categoryFilters, final String[] brandFilters) throws IOException, SQLException {
        try (final Statement stmt = conn.createStatement()) {
            final ArrayList<CartProduct> products = new ArrayList<>();

            final ResultSet rs = stmt.executeQuery(
                    SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("select_all_cart_products")
                            .tableName("products").queryType(SqlQueryType.SELECT).build()));

            while (rs.next()) {
                final String name = rs.getString("name");
                final String categoryName = rs.getString("category_name");
                final String brandName = rs.getString("brand_name");

                if (!Filter.stringMatches(nameFilter, name) || !Filter.isInArray(categoryName, categoryFilters)
                        || !Filter.isInArray(brandName, brandFilters)) {
                    continue;
                }

                final CartProduct product = CartProduct.builder().id(rs.getInt("_product_id")).name(name)
                        .displayImage(rs.getString("display_image")).netPrice(rs.getBigDecimal("net_price"))
                        .quantityInHand(rs.getInt("quantity_in_hand")).build();

                products.add(product);
            }

            return products;
        }
    }

    @Override
    public ArrayList<Product> getAllProducts(final Connection conn, final String nameFilter,
            final String[] categoryFilters, final String[] brandFilters) throws SQLException, IOException {
        try (final Statement stmt = conn.createStatement()) {
            final ArrayList<Product> products = new ArrayList<>();

            final ResultSet rs = stmt.executeQuery(SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                    .fileName("select_all_products").tableName("products").queryType(SqlQueryType.SELECT).build()));

            while (rs.next()) {
                final String name = rs.getString("name");
                final String categoryName = rs.getString("category_name");
                final String brandName = rs.getString("brand_name");

                if (!Filter.stringMatches(nameFilter, name) || !Filter.isInArray(categoryName, categoryFilters)
                        || !Filter.isInArray(brandName, brandFilters)) {
                    continue;
                }

                final ProductCategory productCategory = ProductCategory.builder().name(categoryName)
                        .id(rs.getInt("_product_category_id")).build();
                final ProductBrand productBrand = ProductBrand.builder().name(brandName)
                        .id(rs.getInt("_product_brand_id")).build();
                final Product product = Product.builder().id(rs.getInt("_product_id")).category(productCategory)
                        .brand(productBrand).name(name).description(rs.getString("description"))
                        .displayImage(rs.getString("display_image")).markupRate(rs.getBigDecimal("markup_rate"))
                        .netPrice(rs.getBigDecimal("net_price")).avgCost(rs.getBigDecimal("average_cost"))
                        .avgCostVatRate(rs.getBigDecimal("average_cost_vat_rate"))
                        .quantityInHand(rs.getInt("quantity_in_hand")).isActive(rs.getBoolean("is_active"))
                        .isDeleted(rs.getBoolean("is_deleted")).build();

                products.add(product);
            }

            return products;
        }
    }
}
