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
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.products.ProductDao;
import kompeter.database.dto.products.CartProduct;
import kompeter.lib.logger.KompeterLogger;
import lombok.Getter;

@Getter
public class CartProductDisplayList {
    private static final Logger LOGGER = KompeterLogger.getLogger(CartProductDisplayList.class);

    private String[] brandFilters;
    private String[] categoryFilters;
    private String nameFilter;
    private final AtomicReference<ArrayList<CartProduct>> products;

    private final PropertyChangeSupport propertyChangeSupport;

    public CartProductDisplayList() {
        brandFilters = new String[] {};
        categoryFilters = new String[] {};
        nameFilter = "";
        products = new AtomicReference<>(new ArrayList<>());
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void reloadProducts() {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final ProductDao productDao = factory.getProductDao();

        try (Connection conn = factory.getConnection()) {
            final Object copy = products.getAcquire().clone();

            products.set(productDao.getAllCartProducts(conn, nameFilter, categoryFilters, brandFilters));
            propertyChangeSupport.firePropertyChange("products", copy, products.getAcquire());
        } catch (SQLException | IOException err) {
            LOGGER.severe(
                    String.format("Trying to load products in ProductList, but met with error: %s", err.getMessage()));
        }
    }

    public void setBrandFilters(String[] brandFilters) {
        if (brandFilters == null) {
            brandFilters = new String[] {};
        }

        propertyChangeSupport.firePropertyChange("brandFilters", this.brandFilters, brandFilters);
        this.brandFilters = brandFilters;
        reloadProducts();
    }

    public void setCategoryFilters(String[] categoryFilters) {
        if (categoryFilters == null) {
            categoryFilters = new String[] {};
        }

        propertyChangeSupport.firePropertyChange("categoryFilters", this.categoryFilters, categoryFilters);
        this.categoryFilters = categoryFilters;
        reloadProducts();
    }

    public void setNameFilter(String nameFilter) {
        if (nameFilter == null) {
            nameFilter = "";
        }

        propertyChangeSupport.firePropertyChange("nameFilter", this.nameFilter, nameFilter);
        this.nameFilter = nameFilter;
        reloadProducts();
    }
}
