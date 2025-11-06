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
import lombok.Data;

@Data
@Builder
public class Product {
    private final BigDecimal avgCost;
    private final BigDecimal avgCostVatRate;
    private ProductBrand brand;
    private ProductCategory category;
    private String description;
    private String displayImage;
    private final int id;
    private boolean isActive;
    private boolean isDeleted;
    private BigDecimal markupRate;
    private String name;
    private BigDecimal netPrice;
    private int quantityInHand;
}
