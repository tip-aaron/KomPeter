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
import java.util.logging.Logger;

import kompeter.lib.logger.KompeterLogger;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class Discount {
    private static Logger LOGGER = KompeterLogger.getLogger(Discount.class);

    private BigDecimal amount;
    @Setter
    private String discountType;

    private final PropertyChangeSupport propertyChangeSupport;

    @Builder
    public Discount(final BigDecimal amount, final String discountType) {
        this.amount = amount;
        this.discountType = discountType;

        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void setAmount(final BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            LOGGER.warning(String.format("Discount %s is setting amount to %s, but it cannot be < 0", discountType,
                    amount.doubleValue()));

            return;
        }

        this.amount = amount;
    }
}
