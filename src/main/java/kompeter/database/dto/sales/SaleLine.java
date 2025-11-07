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
public class SaleLine {
    final BigDecimal netPrice;
    final int productId;
    final int quantity;
    final int saleId;

    @Builder
    @JsonCreator
    public SaleLine(@JsonProperty("_productId") final int productId, @JsonProperty("_saleId") final int saleId,
            @JsonProperty("quantity") final int quantity, @JsonProperty("netPrice") final BigDecimal netPrice) {
        this.productId = productId;
        this.saleId = saleId;
        this.quantity = quantity;
        this.netPrice = netPrice;
    }
}
