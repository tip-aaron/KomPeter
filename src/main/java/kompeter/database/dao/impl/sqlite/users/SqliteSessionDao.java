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
import java.util.Optional;

import kompeter.database.dao.users.SessionDao;
import kompeter.database.dto.users.Session;
import kompeter.database.dto.users.User;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteSessionDao implements SessionDao {
    @Override
    public int createSession(final Connection conn, final int userId, final String sessionToken)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_session")
                .tableName("sessions").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt("_user_id", userId);
            stmt.setString("session_token", sessionToken);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public boolean exists(final Connection conn, final String sessionToken) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_session_exists").tableName("sessions").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sessionToken);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }

    @Override
    public Optional<Session> getById(final Connection conn, final int id) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_session_by_id").tableName("sessions").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            final ResultSet rs = stmt.executeQuery();

            return rs.next()
                    ? Optional.of(Session.builder().id(rs.getInt("_session_id"))
                            .createdAt(rs.getTimestamp("_created_at")).expiresAt(rs.getTimestamp("expires_at"))
                            .sessionToken("session_token")
                            .user(User.builder().id(rs.getInt("user_id")).displayName(rs.getString("display_name"))
                                    .firstName(rs.getString("first_name")).lastName(rs.getString("last_name"))
                                    .fullName(rs.getString("full_name")).displayImage(rs.getString("display_image"))
                                    .roles(rs.getString("roles").split(",")).email(rs.getString("email")).build())
                            .build())
                    : Optional.empty();
        }
    }

    @Override
    public Optional<Session> getByToken(final Connection conn, final String sessionToken)
            throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_session_by_token").tableName("sessions").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sessionToken);

            final ResultSet rs = stmt.executeQuery();

            return rs.next()
                    ? Optional.of(Session.builder().id(rs.getInt("_session_id"))
                            .createdAt(rs.getTimestamp("_created_at")).expiresAt(rs.getTimestamp("expires_at"))
                            .sessionToken("session_token")
                            .user(User.builder().id(rs.getInt("user_id")).displayName(rs.getString("display_name"))
                                    .firstName(rs.getString("first_name")).lastName(rs.getString("last_name"))
                                    .fullName(rs.getString("full_name")).displayImage(rs.getString("display_image"))
                                    .roles(rs.getString("roles").split(",")).email(rs.getString("email")).build())
                            .build())
                    : Optional.empty();
        }
    }

    @Override
    public void removeByToken(final Connection conn, final String sessionToken) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("remove_session_by_token").tableName("sessions").queryType(SqlQueryType.DELETE).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sessionToken);

            stmt.executeUpdate();
        }
    }
}
