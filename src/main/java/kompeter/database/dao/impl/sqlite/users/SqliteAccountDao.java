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

import kompeter.database.dao.users.AccountDao;
import kompeter.database.dto.users.AccountPassword;
import kompeter.database.loader.AQueryLoader.SqlQueryData;
import kompeter.database.loader.AQueryLoader.SqlQueryType;
import kompeter.database.loader.sqlite.SqliteQueryLoader;
import kompeter.database.statement.NamedPreparedStatement;

public class SqliteAccountDao implements AccountDao {
    @Override
    public int createAccount(final Connection conn, final int userId, final String passwordHash,
            final String passwordSalt, final String email) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder().fileName("create_account")
                .tableName("accounts").queryType(SqlQueryType.INSERT).build());

        try (NamedPreparedStatement stmt = new NamedPreparedStatement(conn, query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt("_user_id", userId);
            stmt.setString("password_hash", passwordHash);
            stmt.setString("password_salt", passwordSalt);
            stmt.setString("email", email);

            stmt.executeUpdate();

            final ResultSet rs = stmt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public Optional<AccountPassword> getAccountPassword(final Connection conn, final String email)
            throws IOException, SQLException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_account_password").tableName("accounts").queryType(SqlQueryType.SELECT).build());

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);

            final ResultSet rs = stmt.executeQuery();

            return rs.next()
                    ? Optional.of(AccountPassword.builder().passwordHash(rs.getString("password_hash"))
                            .passwordSalt(rs.getString("password_salt")).build())
                    : Optional.empty();
        }
    }

    @Override
    public int getUserIdByEmail(final Connection conn, final String email) throws SQLException, IOException {
        final String query = SqliteQueryLoader.getInstance().getQuery(SqlQueryData.builder()
                .fileName("select_user_id_by_email").tableName("accounts").queryType(SqlQueryType.SELECT).build());
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);

            final ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getInt("_user_id") : -1;
        }
    }
}
