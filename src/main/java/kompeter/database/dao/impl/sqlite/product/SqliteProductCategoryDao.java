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

import kompeter.database.dao.products.ProductCategoryDao;
import kompeter.database.dto.products.ProductCategory;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteProductCategoryDao implements ProductCategoryDao {
    @Override
    public int createProductCategory(final Connection conn, final String name) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_category")
                .tableName("product_categories").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString("name", name);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public ArrayList<ProductCategory> getAllProductCategories(final Connection conn) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance()
                .getQuery(SqlQueryData.builder().fileName("select_all_categories").tableName("product_categories")
                        .queryType(SqlQueryType.SELECT).build());

        try (Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery(query);
            final ArrayList<ProductCategory> res = new ArrayList<>();

            while (rs.next()) {
                res.add(ProductCategory.builder().id(rs.getInt("_product_category_id")).name(rs.getString("name"))
                        .build());
            }

            return res;
        }
    }
}
