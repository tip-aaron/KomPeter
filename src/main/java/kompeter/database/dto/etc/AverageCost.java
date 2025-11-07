package kompeter.database.dto.etc;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AverageCost {
    final BigDecimal avgCost;
    final BigDecimal avgVatRate;
}
