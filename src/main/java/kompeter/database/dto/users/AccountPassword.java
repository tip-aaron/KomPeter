/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountPassword {
    final String passwordHash;
    final String passwordSalt;
}
