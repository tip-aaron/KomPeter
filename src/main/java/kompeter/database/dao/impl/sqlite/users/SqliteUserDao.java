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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import kompeter.database.dao.users.UserDao;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteUserDao implements UserDao {
    @Override
    public int createUser(final Connection conn, final String displayName, final String firstName,
            final String lastName) throws IOException, SQLException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_user")
                .tableName("users").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString("display_name", displayName);
            stmt.setString("first_name", firstName);
            stmt.setString("last_name", lastName);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public boolean isDisplayNameTaken(final Connection conn, final String displayName)
            throws IOException, SQLException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_display_name_taken").tableName("users").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, displayName);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }
}
