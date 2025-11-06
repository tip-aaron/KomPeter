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

public interface UserDao {
    int createUser(Connection conn, String displayName, String firstName, String lastName)
            throws IOException, SQLException;

    boolean isDisplayNameTaken(Connection conn, String displayName) throws IOException, SQLException;
}
