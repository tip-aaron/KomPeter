/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.products;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Product {
    private final BigDecimal avgCost;
    private final BigDecimal avgCostVatRate;
    private ProductBrand brand;
    private ProductCategory category;
    @Default
    private String description = "";
    private String displayImage;
    private final int id;
    @Default
    private boolean isActive = false;
    @Default
    private boolean isDeleted = false;
    private BigDecimal markupRate;
    @Default
    private int minimumQuantity = 0;
    private String name;
    @Default
    private BigDecimal netPrice = BigDecimal.ZERO;
    @Default
    private int quantityInHand = 0;
}
