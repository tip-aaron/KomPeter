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
    private final String displayImage;
    @Getter
    private final int id;
    @Getter
    private final String name;
    @Getter
    private final BigDecimal netPrice;
    @Getter
    private final PropertyChangeSupport propertyChangeSupport;
    private final AtomicInteger quantityInCart;

    @Getter
    private final int quantityInHand;

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

    public int getAvailableQuantity() {
        return quantityInHand - quantityInCart.getAcquire();
    }

    public int getQuantityInCart() {
        return quantityInCart.getAcquire();
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
