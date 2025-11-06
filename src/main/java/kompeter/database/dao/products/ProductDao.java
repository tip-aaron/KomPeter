/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.products;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import kompeter.database.dto.products.CartProduct;
import kompeter.database.dto.products.Product;

public interface ProductDao {
    ArrayList<CartProduct> getAllCartProducts(Connection conn, String nameFilter, String[] categoryFilters,
            String[] brandFilters) throws IOException, SQLException;

    ArrayList<Product> getAllProducts(Connection conn, String nameFilter, String[] categoryFilters,
            String[] brandFilters) throws SQLException, IOException;
}
