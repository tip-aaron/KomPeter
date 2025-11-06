/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.products;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductNameSearch {
    private final int id;
    private final String name;
}
