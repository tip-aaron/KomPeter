/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.purchases;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
public class PurchaseOrderLine {
    final int productId;
    final int purchaseOrderId;
    final int quantity;
    final BigDecimal unitPrice;

    @Builder
    @JsonCreator
    public PurchaseOrderLine(@JsonProperty("_purchaseOrderId") final int purchaseOrderId,
            @JsonProperty("_productId") final int productId, @JsonProperty("unitPrice") final BigDecimal unitPrice,
            @JsonProperty("quantity") final int quantity) {
        this.purchaseOrderId = purchaseOrderId;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
}
