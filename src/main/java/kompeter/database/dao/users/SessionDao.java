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

import kompeter.database.dto.users.Session;

public interface SessionDao {

    int createSession(Connection conn, int userId, String sessionToken) throws SQLException, IOException;

    boolean exists(Connection conn, String sessionToken) throws SQLException, IOException;

    Optional<Session> getById(Connection conn, int id) throws SQLException, IOException;

    Optional<Session> getByToken(Connection conn, String sessionToken) throws SQLException, IOException;

    void removeByToken(Connection conn, String sessionToken) throws SQLException, IOException;
}
