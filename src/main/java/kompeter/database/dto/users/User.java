/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.users;

import kompeter.lib.helper.Filter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String displayImage;
    String displayName;
    final String email;
    String firstName;
    String fullName;
    final int id;
    String lastName;
    Role[] roles;

    public boolean isAdmin() {
        return Filter.isInArray("admin", roles);
    }

    public boolean isCashier() {
        return Filter.isInArray("cashier", roles);
    }

    public boolean isInventoryClerk() {
        return Filter.isInArray("inventory_clerk", roles);
    }
}
