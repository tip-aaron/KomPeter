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

import kompeter.database.dao.products.ProductBrandDao;
import kompeter.database.dto.products.ProductBrand;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteProductBrandDao implements ProductBrandDao {
    @Override
    public int createProductBrand(final Connection conn, final String name) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_brand")
                .tableName("product_brands").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString("name", name);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public ArrayList<ProductBrand> getAllProductBrands(final Connection conn) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_all_brands").tableName("product_brands").queryType(SqlQueryType.SELECT).build());

        try (Statement stmt = conn.createStatement()) {
            final ResultSet rs = stmt.executeQuery(query);
            final ArrayList<ProductBrand> res = new ArrayList<>();

            while (rs.next()) {
                res.add(ProductBrand.builder().id(rs.getInt("_product_brand_id")).name(rs.getString("name")).build());
            }

            return res;
        }
    }
}
