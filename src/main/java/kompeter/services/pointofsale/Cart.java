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
import java.util.ArrayList;
import java.util.logging.Logger;

import kompeter.database.dto.products.CartProduct;
import kompeter.lib.logger.KompeterLogger;
import lombok.Getter;

@Getter
public class Cart {
    private static final Logger LOGGER = KompeterLogger.getLogger(Cart.class);

    private final ArrayList<Discount> discounts;
    private final ArrayList<CartProduct> products;
    private final PropertyChangeSupport propertyChangeSupport;

    public Cart() {
        products = new ArrayList<>();
        discounts = new ArrayList<>();
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addDiscount(final Discount discount) {
        final Object copy = discounts.clone();

        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);
        discounts.add(discount);
        LOGGER.info(String.format("Added to cart: ", discount));
    }

    public void addProduct(final CartProduct product) {
        final Object copy = products.clone();

        propertyChangeSupport.firePropertyChange("products", copy, products);
        products.add(product);
        LOGGER.info(String.format("Added to cart: ", product));
    }

    public void addQty(final int pId, final int qty) {
        for (final CartProduct product : products) {
            if (product.getId() == pId) {
                product.setQuantityInCart(product.getQuantityInCart() + qty);

                break;
            }
        }
    }

    public void clearDiscounts() {
        final Object copy = discounts.clone();

        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);
        discounts.clear();
        LOGGER.info("Cleared discounts in cart");
    }

    public void clearProducts() {
        final Object copy = products.clone();

        propertyChangeSupport.firePropertyChange("products", copy, products);
        products.clear();
        LOGGER.info("Cleared products in cart");
    }

    public void decQty(final int pId, final int qty) {
        for (final CartProduct product : products) {
            if (product.getId() == pId) {
                product.setQuantityInCart(product.getQuantityInCart() - qty);

                break;
            }
        }
    }

    public boolean exists(final int id) {
        for (final CartProduct product : products) {
            if (product.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public BigDecimal getTotalNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;

        for (final CartProduct product : products) {
            netPrice = netPrice.add(product.getNetPrice());
        }

        for (final Discount discount : discounts) {
            netPrice = netPrice.subtract(discount.getAmount());
        }

        return netPrice;
    }

    public BigDecimal getTotalPrice(final BigDecimal vatRate) {
        return getTotalNetPrice().add(getTotalVatPrice(vatRate));
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;

        for (final CartProduct product : products) {
            totalQuantity += product.getQuantityInCart();
        }

        return totalQuantity;
    }

    public BigDecimal getTotalVatPrice(final BigDecimal vatRate) {
        return getTotalNetPrice().multiply(vatRate.add(BigDecimal.ONE));
    }

    public void removeDiscount(final Discount discount) {
        final Object copy = discounts.clone();

        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);
        discounts.remove(discount);
        LOGGER.info(String.format("Removed from cart: %s", discount));
    }

    public void removeProduct(final CartProduct product) {
        final Object copy = products.clone();

        propertyChangeSupport.firePropertyChange("products", copy, products);
        products.remove(product);
        LOGGER.info(String.format("Removed from cart: %s", product));
    }

    public void removeProduct(final int pId) {
        products.stream().filter((p) -> p.getId() == pId).findFirst().ifPresentOrElse((final CartProduct p) -> {
            final Object copy = products.clone();

            propertyChangeSupport.firePropertyChange("products", copy, products);
            products.remove(p);
            LOGGER.info(String.format("Removed from cart: %s", p));
        }, () -> {
            LOGGER.warning(String.format("Removing a product with ID %d, but it does not exist in cart.", pId));
        });
    }
}
