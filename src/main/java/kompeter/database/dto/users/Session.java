/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dto.users;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Session {
    final Timestamp createdAt;
    final Timestamp expiresAt;
    final int id;
    final String sessionToken;
    final User user;

    public boolean isExpired() {
        return expiresAt.after(createdAt);
    }
}
