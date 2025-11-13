/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.services.pointofsale;

import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.products.ProductDao;
import kompeter.database.dto.products.CartProduct;
import kompeter.lib.helper.Filter;
import kompeter.lib.logger.KompeterLogger;
import lombok.Getter;

@Getter
public class CartProductDisplayList {
    private static final Logger LOGGER = KompeterLogger.getLogger(CartProductDisplayList.class);

    private final AtomicReference<String[]> brandFilters;
    private final AtomicReference<String[]> categoryFilters;
    private final AtomicReference<String> nameFilter;
    /**
     * 
     * The displayed products
     * 
     */
    private final AtomicReference<ArrayList<CartProduct>> products;
    private final AtomicReference<ArrayList<CartProduct>> masterProducts;

    private final PropertyChangeSupport propertyChangeSupport;

    public CartProductDisplayList() {
        brandFilters = new AtomicReference<>(new String[] {});
        categoryFilters = new AtomicReference<>(new String[] {});
        nameFilter = new AtomicReference<>("");
        masterProducts = new AtomicReference<>(new ArrayList<>());
        products = new AtomicReference<>(new ArrayList<>());
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void removeProduct(final CartProduct p) {
        final Object copy = products.getAcquire().clone();
        final Object copy2 = products.getAcquire().clone();

        products.getAcquire().remove(p);
        masterProducts.getAcquire().remove(p);

        propertyChangeSupport.firePropertyChange("products", copy, products.getAcquire());
        propertyChangeSupport.firePropertyChange("masterProducts", copy2, masterProducts.getAcquire());
    }

    private CartProduct findProductById(final int id, final List<CartProduct> list) {
        for (final CartProduct p : list) {
            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }

    /**
     * if there are items stored in the cart, it may be possible to lose its
     * reference when
     * searching as we rebuild the list of products, so we reconciliate them.
     * 
     * For now, we just have a display and a master list
     */
    private void reconciliate(final ArrayList<CartProduct> newProducts) {
        final ArrayList<CartProduct> masterProducts = this.masterProducts.getAcquire();

        for (final CartProduct newProduct : newProducts) {
            final CartProduct existing = findProductById(newProduct.getId(), masterProducts);

            if (existing != null) {
                existing.updateFrom(newProduct);
            } else {
                masterProducts.add(newProduct);
            }
        }
    }

    private void applySearch() {
        final ArrayList<CartProduct> products = this.products.getAcquire();
        final Object copy = products.clone();
        final String nameFilter = this.nameFilter.getAcquire();

        products.clear();

        for (final CartProduct cartProduct : masterProducts.getAcquire()) {
            final boolean nameBool = nameFilter.length() == 0
                    || Filter.stringMatches(nameFilter, cartProduct.getName());

            if (nameBool) {
                products.add(cartProduct);
            }
        }

        propertyChangeSupport.firePropertyChange("products", copy, products);
    }

    public void reloadProducts() {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final ProductDao productDao = factory.getProductDao();

        try (Connection conn = factory.getConnection()) {
            final Object copy = masterProducts.getAcquire().clone();
            final ArrayList<CartProduct> sample = new ArrayList<>();

            sample.add(CartProduct.builder()
                    .id(1).name("Aaron").netPrice(new BigDecimal("1000")).quantityInHand(20)
                    .build());

            sample.add(CartProduct.builder()
                    .id(2).name("Hatdog").netPrice(new BigDecimal("2000")).quantityInHand(20)
                    .build());

            sample.add(CartProduct.builder()
                    .id(3).name("Hatchuu").netPrice(new BigDecimal("1000")).quantityInHand(10)
                    .build());

            // reconciliate(productDao.getAllCartProducts(conn));
            reconciliate(sample);
            propertyChangeSupport.firePropertyChange("masterProducts", copy, masterProducts.getAcquire());
            applySearch();
        } catch (final SQLException /* | IOException */ err) {
            LOGGER.severe(
                    String.format("Trying to load products in ProductList, but met with error: %s", err.getMessage()));
        }
    }

    public void setBrandFilters(String[] brandFilters) {
        if (brandFilters == null) {
            brandFilters = new String[] {};
        }

        propertyChangeSupport.firePropertyChange("brandFilters", this.brandFilters, brandFilters);
        this.brandFilters.set(brandFilters);
        applySearch();
    }

    public void setCategoryFilters(String[] categoryFilters) {
        if (categoryFilters == null) {
            categoryFilters = new String[] {};
        }

        propertyChangeSupport.firePropertyChange("categoryFilters", this.categoryFilters, categoryFilters);
        this.categoryFilters.set(categoryFilters);
        applySearch();
    }

    public void setNameFilter(String nameFilter) {
        if (nameFilter == null) {
            nameFilter = "";
        }

        propertyChangeSupport.firePropertyChange("nameFilter", this.nameFilter, nameFilter);
        this.nameFilter.set(nameFilter);
        applySearch();
    }
}
