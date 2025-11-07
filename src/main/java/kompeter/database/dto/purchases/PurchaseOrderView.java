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

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseOrderView {
    final int _purchaseOrderId;
    final String purchaseCode;
    final Timestamp purchaseDate;
    final BigDecimal totalCost;
    final int totalProducts;
    final int totalQuantity;
    final BigDecimal vatRate;
}
