/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.purchases;

import java.math.BigDecimal;
import java.sql.Timestamp;

import kompeter.database.dto.etc.Supplier;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseOrder {
    final int id;
    final PurchaseOrderLine[] lines;
    final String purchaseCode;
    final Timestamp purchaseDate;
    final Supplier supplier;
    final BigDecimal vatRate;

    public BigDecimal getTotalCost() {
        BigDecimal cost = BigDecimal.ZERO;

        for (final PurchaseOrderLine line : lines) {
            cost = cost.add(line.getUnitPrice().multiply(new BigDecimal(line.getQuantity())));
        }

        return cost.multiply(vatRate.add(BigDecimal.ONE));
    }
}
