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
    final BigDecimal vatRate;
    final int totalProducts;
    final int totalQuantity;
    final BigDecimal totalCost;
}
