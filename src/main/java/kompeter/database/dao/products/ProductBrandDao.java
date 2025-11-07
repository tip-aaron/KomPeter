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

import kompeter.database.dto.products.ProductBrand;

public interface ProductBrandDao {
    int createProductBrand(Connection conn, String name) throws SQLException, IOException;

    ArrayList<ProductBrand> getAllProductBrands(Connection conn) throws SQLException, IOException;
}
