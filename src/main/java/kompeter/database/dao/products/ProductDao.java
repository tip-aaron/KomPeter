/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.products;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import kompeter.database.dto.etc.AverageCost;
import kompeter.database.dto.products.CartProduct;
import kompeter.database.dto.products.Product;

public interface ProductDao {
    int createProduct(Connection conn, Product product) throws SQLException, IOException;

    ArrayList<CartProduct> getAllCartProducts(Connection conn, String nameFilter, String[] categoryFilters,
            String[] brandFilters) throws IOException, SQLException;

    ArrayList<Product> getAllProducts(Connection conn, String nameFilter, String[] categoryFilters,
            String[] brandFilters) throws SQLException, IOException;

    Optional<AverageCost> getAvgCost(Connection conn, int productId) throws SQLException, IOException;

    void changeSellingPrice(Connection conn, int productId, BigDecimal newPrice,
            BigDecimal avgCost, BigDecimal avgCostVatRate) throws SQLException, IOException;

    void addQuantity(Connection conn, int productId, int toAdd) throws SQLException, IOException;

    void decQuantity(Connection conn, int productId, int toDec) throws SQLException, IOException;

    Optional<BigDecimal> getMarkupRate(Connection conn, int productId) throws SQLException, IOException;

    boolean exists(Connection conn, int productId) throws SQLException, IOException;

    boolean exists(Connection conn, String name) throws SQLException, IOException;
}
