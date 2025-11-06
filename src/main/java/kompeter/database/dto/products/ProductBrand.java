/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductBrand {
    private final int id;
    private String name;
}
