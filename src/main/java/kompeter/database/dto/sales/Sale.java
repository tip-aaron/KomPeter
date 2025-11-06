/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.sales;

import java.math.BigDecimal;
import java.sql.Timestamp;

import kompeter.database.dto.etc.Customer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sale {
    final Customer customer;
    final int id;
    final String saleCode;
    final Timestamp saleDate;
    final SaleDiscount[] saleDiscounts;
    final SaleLine[] saleLines;
    final BigDecimal vatRate;

    public BigDecimal getDiscount() {
        BigDecimal discount = BigDecimal.ZERO;

        for (final SaleDiscount saleDiscount : saleDiscounts) {
            discount = discount.add(saleDiscount.getAmount());
        }

        return discount;
    }

    public BigDecimal getNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;

        for (final SaleLine saleLine : saleLines) {
            netPrice = netPrice.add(saleLine.getNetPrice());
        }

        return netPrice.subtract(getDiscount());
    }

    public BigDecimal getTotalPrice() {
        return getNetPrice().multiply(vatRate);
    }
}
