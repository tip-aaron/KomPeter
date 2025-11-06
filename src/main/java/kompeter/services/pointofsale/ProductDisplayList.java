/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.services.pointofsale;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.products.ProductDao;
import kompeter.database.dto.products.CartProduct;
import kompeter.lib.logger.KompeterLogger;
import lombok.Getter;

@Getter
public class ProductDisplayList {
    private static final Logger LOGGER = KompeterLogger.getLogger(ProductDisplayList.class);

    private String[] brandFilters;
    private String[] categoryFilters;
    private String nameFilter;
    private ArrayList<CartProduct> products;

    private final PropertyChangeSupport propertyChangeSupport;

    public ProductDisplayList() {
        products = null;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void reloadProducts() {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final ProductDao productDao = factory.getProductDao();

        try (Connection conn = factory.getConnection()) {
            products = productDao.getAllCartProducts(conn, nameFilter, categoryFilters, brandFilters);
        } catch (SQLException | IOException err) {
            LOGGER.severe(
                    String.format("Trying to load products in ProductList, but met with error: %s", err.getMessage()));
        }
    }

    public void setBrandFilters(final String[] brandFilters) {
        propertyChangeSupport.firePropertyChange("brandFilters", this.brandFilters, brandFilters);
        this.brandFilters = brandFilters;
        reloadProducts();
    }

    public void setCategoryFilters(final String[] categoryFilters) {
        propertyChangeSupport.firePropertyChange("categoryFilters", this.categoryFilters, categoryFilters);
        this.categoryFilters = categoryFilters;
        reloadProducts();
    }

    public void setNameFilter(final String nameFilter) {
        propertyChangeSupport.firePropertyChange("nameFilter", this.nameFilter, nameFilter);
        this.nameFilter = nameFilter;
        reloadProducts();
    }
}
