/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.dao.impl.sqlite.users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kompeter.database.dao.users.RoleDao;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteRoleDao implements RoleDao {

    @Override
    public int addRole(final Connection conn, final String name) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("remove_role")
                .tableName("roles").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString("name", name);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public void addUserRole(final Connection conn, final int roleId, final int userId)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("add_user_role")
                .tableName("roles").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query)) {
            stmt.setInt("_role_id", roleId);
            stmt.setInt("_user_id", userId);

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteRole(final Connection conn, final int roleId) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("remove_role")
                .tableName("roles").queryType(SqlQueryType.DELETE).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query)) {
            stmt.setInt("_role_id", roleId);

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeUserRole(final Connection conn, final int roleId, final int userId)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("remove_user_role").tableName("roles").queryType(SqlQueryType.DELETE).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query)) {
            stmt.setInt("_role_id", roleId);
            stmt.setInt("_user_id", userId);

            stmt.executeUpdate();
        }
    }
}
