/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import kompeter.database.dto.users.AccountPassword;

public interface AccountDao {
    int createAccount(Connection conn, int userId, String passwordHash, String passwordSalt, String email)
            throws SQLException, IOException;

    Optional<AccountPassword> getAccountPassword(Connection conn, String email) throws IOException, SQLException;

    /** Returns -1 if not found */
    int getUserIdByEmail(Connection conn, String email) throws SQLException, IOException;
}
