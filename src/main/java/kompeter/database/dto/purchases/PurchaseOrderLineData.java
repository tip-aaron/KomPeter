package kompeter.database.dto.purchases;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseOrderLineData {
    final int productId;
    final int quantity;
    final BigDecimal unitPrice;
}
