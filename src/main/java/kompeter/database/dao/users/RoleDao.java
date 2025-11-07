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

public interface RoleDao {
    int addRole(final Connection conn, final String name) throws SQLException, IOException;

    void addUserRole(final Connection conn, final int roleId, int userId) throws SQLException, IOException;

    void deleteRole(final Connection conn, final int roleId) throws SQLException, IOException;

    void removeUserRole(final Connection conn, final int roleId, int userId) throws SQLException, IOException;
}
