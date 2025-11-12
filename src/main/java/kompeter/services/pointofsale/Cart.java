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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import kompeter.database.dto.products.CartProduct;
import kompeter.lib.logger.KompeterLogger;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Cart {
    private static final Logger LOGGER = KompeterLogger.getLogger(Cart.class);

    private final AtomicReference<ArrayList<Discount>> discounts;
    private final AtomicReference<ArrayList<CartProduct>> products;
    private final PropertyChangeSupport propertyChangeSupport;

    public Cart() {
        products = new AtomicReference<>(new ArrayList<>());
        discounts = new AtomicReference<>(new ArrayList<>());
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public boolean isEmpty() {
        return products.getAcquire().size() == 0 && discounts.getAcquire().size() == 0;
    }

    public void addDiscount(final Discount discount) {
        final ArrayList<Discount> discounts = this.discounts.getAcquire();

        final Object copy = discounts.clone();

        discounts.add(discount);
        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);

        LOGGER.info(String.format("Added to cart: ", discount));
    }

    public void addProduct(final CartProduct product) {
        final ArrayList<CartProduct> products = this.products.getAcquire();

        final Object copy = products.clone();

        products.add(product);
        propertyChangeSupport.firePropertyChange("products", copy, products);

        LOGGER.info(String.format("Added to cart: ", product));
    }

    public void addQty(final int pId, final int qty) {
        for (final CartProduct product : products.getAcquire()) {
            if (product.getId() == pId) {
                product.setQuantityInCart(product.getQuantityInCart() + qty);

                break;
            }
        }
    }

    public void clearDiscounts() {
        final ArrayList<Discount> discounts = this.discounts.getAcquire();
        final Object copy = discounts.clone();

        discounts.clear();
        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);

        LOGGER.info("Cleared discounts in cart");
    }

    public void clearProducts() {
        final ArrayList<CartProduct> products = this.products.getAcquire();
        final Object copy = products.clone();

        products.clear();
        propertyChangeSupport.firePropertyChange("products", copy, products);

        for (final CartProduct products2 : products) {
            products2.setQuantityInCart(0);
        }

        LOGGER.info("Cleared products in cart");
    }

    public void decQty(final int pId, final int qty) {
        for (final CartProduct product : products.getAcquire()) {
            if (product.getId() == pId) {
                product.setQuantityInCart(product.getQuantityInCart() - qty);

                break;
            }
        }
    }

    public boolean exists(final int id) {
        for (final CartProduct product : products.getAcquire()) {
            if (product.getId() == id) {
                return true;
            }
        }

        return false;
    }

    public Optional<CartProduct> getProduct(final int id) {
        for (final CartProduct product : products.getAcquire()) {
            if (product.getId() == id) {
                return Optional.of(product);
            }
        }

        return Optional.empty();
    }

    public BigDecimal getTotalDiscountPrice() {
        BigDecimal discountPrice = BigDecimal.ZERO;

        for (final Discount discount : discounts.getAcquire()) {
            discountPrice = discountPrice.add(discount.getAmount());
        }

        return discountPrice;
    }

    public BigDecimal getTotalNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;

        for (final CartProduct product : products.getAcquire()) {
            netPrice = netPrice.add(product.getTotalNetPrice());
        }

        return netPrice;
    }

    public BigDecimal getTotalDiscountedNetPrice() {
        return getTotalNetPrice().subtract(getTotalDiscountPrice());
    }

    public BigDecimal getTotalPrice(final BigDecimal vatRate) {
        return getTotalDiscountedNetPrice().add(getTotalVatPrice(vatRate));
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;

        for (final CartProduct product : products.getAcquire()) {
            totalQuantity += product.getQuantityInCart();
        }

        return totalQuantity;
    }

    public BigDecimal getTotalVatPrice(final BigDecimal vatRate) {
        return getTotalDiscountedNetPrice().multiply(vatRate.add(BigDecimal.ONE));
    }

    public void removeDiscount(final Discount discount) {
        final ArrayList<Discount> discounts = this.discounts.getAcquire();
        final Object copy = discounts.clone();

        discounts.remove(discount);
        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);

        LOGGER.info(String.format("Removed from cart: %s", discount));
    }

    public void removeDiscounts(final ArrayList<Discount> toRemove) {
        final ArrayList<Discount> discounts = this.discounts.getAcquire();
        final Object copy = discounts.clone();

        discounts.removeAll(toRemove);
        propertyChangeSupport.firePropertyChange("discounts", copy, discounts);

        LOGGER.info(String.format("Removed from cart: %s", toRemove));
    }

    public void removeProduct(final CartProduct product) {
        final ArrayList<CartProduct> products = this.products.getAcquire();

        final Object copy = products.clone();

        products.remove(product);
        propertyChangeSupport.firePropertyChange("products", copy, products);

        product.setQuantityInCart(0);

        LOGGER.info(String.format("Removed from cart: %s", product));
    }

    public void removeProduct(final int pId) {
        final ArrayList<CartProduct> products = this.products.getAcquire();

        products.stream().filter((p) -> p.getId() == pId).findFirst().ifPresentOrElse((final CartProduct p) -> {
            final Object copy = products.clone();

            products.remove(p);
            propertyChangeSupport.firePropertyChange("products", copy, products);

            p.setQuantityInCart(0);

            LOGGER.info(String.format("Removed from cart: %s", p));
        }, () -> {
            LOGGER.warning(String.format("Removing a product with ID %d, but it does not exist in cart.", pId));
        });
    }
}
