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

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleView {
    final String customerName;
    // without discount
    final BigDecimal grossTotalNetPrice;
    final String saleCode;
    final Timestamp saleDate;
    final int saleId;
    final BigDecimal totalDiscount;
    final BigDecimal totalNetPrice;
    final int totalProducts;
    final int totalQuantity;
    final BigDecimal vatRate;
}
