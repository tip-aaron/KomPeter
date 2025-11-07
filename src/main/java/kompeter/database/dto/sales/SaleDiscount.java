/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.sales;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
public class SaleDiscount {
    final BigDecimal amount;
    final String discountType;
    final int id;
    final int saleId;

    @Builder
    @JsonCreator
    public SaleDiscount(@JsonProperty("amount") final BigDecimal amount,
            @JsonProperty("discountType") final String discountType, @JsonProperty("_saleDiscountId") final int id,
            @JsonProperty("_saleId") final int saleId) {
        this.amount = amount;
        this.discountType = discountType;
        this.id = id;
        this.saleId = saleId;
    }
}
