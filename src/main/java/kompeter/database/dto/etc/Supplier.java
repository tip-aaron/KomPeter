/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.etc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Supplier {
    final int id;
    final String name;
}
