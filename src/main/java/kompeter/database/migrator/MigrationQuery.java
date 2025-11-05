/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.migrator;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MigrationQuery {
    String name, query;
    int version;
}
