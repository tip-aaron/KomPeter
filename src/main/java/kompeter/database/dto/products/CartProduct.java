/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.products;

import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import kompeter.lib.logger.KompeterLogger;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
public class CartProduct {
    private static Logger LOGGER = KompeterLogger.getLogger(CartProduct.class);

    @Getter
    private String displayImage;
    @Getter
    private final int id;
    @Getter
    private String name;
    @Getter
    private BigDecimal netPrice;
    @Getter
    private final PropertyChangeSupport propertyChangeSupport;
    private final AtomicInteger quantityInCart;

    @Getter
    private int quantityInHand;

    @Builder
    public CartProduct(final int id, final String name, final String displayImage, final BigDecimal netPrice,
            final int quantityInHand) {
        this.id = id;
        this.name = name;
        this.displayImage = displayImage;
        this.netPrice = netPrice;
        this.quantityInHand = quantityInHand;
        this.quantityInCart = new AtomicInteger(0);

        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Useful for reconciliations from search functionalities
     * 
     * @param another
     */
    public void updateFrom(final CartProduct another) {
        if (this.getId() != another.getId()) {
            throw new IllegalArgumentException("Another must be of the same type when calling updateFrom");
        }

        final int oldQty = this.quantityInHand;
        final BigDecimal oldNp = this.netPrice;
        final String oldDp = this.displayImage;
        final String oldN = this.name;

        this.quantityInHand = another.getQuantityInHand();
        this.netPrice = another.netPrice;
        this.displayImage = another.displayImage;
        this.name = another.name;

        if (getAvailableQuantity() < 0) {
            setQuantityInCart(this.quantityInHand);
        }

        propertyChangeSupport.firePropertyChange("netPrice", oldNp, another.getNetPrice());
        propertyChangeSupport.firePropertyChange("quantityInHand", oldQty, another.getQuantityInHand());
        propertyChangeSupport.firePropertyChange("displayImage", oldDp, another.getDisplayImage());
        propertyChangeSupport.firePropertyChange("name", oldN, another.getName());
    }

    public boolean isInCart() {
        return quantityInCart.getAcquire() != 0;
    }

    public int getAvailableQuantity() {
        return quantityInHand - quantityInCart.getAcquire();
    }

    public int getQuantityInCart() {
        return quantityInCart.getAcquire();
    }

    public BigDecimal getTotalNetPrice() {
        return netPrice.multiply(new BigDecimal(quantityInCart.getAcquire()));
    }

    public void setQuantityInHand(final int quantityInCart) {
        final int oldQty = this.quantityInHand;

        this.quantityInHand = quantityInCart;

        if (getAvailableQuantity() < 0) {
            setQuantityInCart(this.quantityInHand);
        }

        propertyChangeSupport.firePropertyChange("quantityInHand", oldQty, quantityInCart);
    }

    public void setQuantityInCart(final int quantityInCart) {
        if (quantityInCart > quantityInHand || quantityInCart < 0) {
            LOGGER.warning(
                    String.format("Product %s with id %d is setting quantityInCart to %s, but quantityInHand is %s",
                            name, id, quantityInCart, quantityInHand));

            return;
        }

        final int qty = this.quantityInCart.getAcquire();

        this.quantityInCart.set(quantityInCart);
        propertyChangeSupport.firePropertyChange("quantityInCart", qty, quantityInCart);

        LOGGER.info(String.format("Changed quantity in cart of product with ID %d from %d to %d", id,
                this.quantityInCart.getAcquire(), quantityInCart));
    }
}
