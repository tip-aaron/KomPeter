/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.services.auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import kompeter.constants.PropertyKey;
import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.users.AccountDao;
import kompeter.database.dao.users.SessionDao;
import kompeter.database.dao.users.UserDao;
import kompeter.database.dto.users.AccountPassword;
import kompeter.database.dto.users.Session;
import kompeter.lib.configuration.ApplicationConfig;
import kompeter.lib.cryptography.HashedStringWithSalt;
import kompeter.lib.cryptography.Hasher;
import kompeter.lib.cryptography.PurchaseCodeGenerator;
import kompeter.lib.cryptography.Salt;
import kompeter.lib.logger.KompeterLogger;
import kompeter.utils.CharacterUtils;

public final class Authentication {
    private static final Logger LOGGER = KompeterLogger.getLogger(Authentication.class);

    public static AuthenticationStatus signIn(final String email, final char[] password) {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final AccountDao accountDao = factory.getAccountDao();
        final SessionDao sessionDao = factory.getSessionDao();

        try (Connection conn = factory.getConnection()) {
            final int userId = accountDao.getUserIdByEmail(conn, email);

            if (userId == -1) {
                LOGGER.warning(String.format("Logging with email %s, but it doesn't exist.", email));

                return AuthenticationStatus.builder().message("Invalid Email")
                        .statusType(AuthenticationStatus.StatusType.ERROR).build();
            }

            // we are sure it exists if the check above is skipped
            final AccountPassword accountPassword = accountDao.getAccountPassword(conn, email).get();
            final Optional<HashedStringWithSalt> maybeHashedPass = Hasher.hash(password,
                    Salt.fromBase64(accountPassword.getPasswordSalt()));

            if (maybeHashedPass.isEmpty()) {
                LOGGER.severe(
                        String.format("Trying to hash provided password for %s, but something went wrong", email));

                return AuthenticationStatus.builder().message("Something went wrong.")
                        .statusType(AuthenticationStatus.StatusType.ERROR).build();
            }

            final HashedStringWithSalt hashedPass = maybeHashedPass.get();
            final byte[] accountPassBytes = Base64.getDecoder().decode(accountPassword.getPasswordHash());

            if (!CharacterUtils.constantTimeEquals(hashedPass.hashedString(), accountPassBytes)) {
                LOGGER.warning(String.format("Trying to login, but got incorrect password for %s", email));

                return AuthenticationStatus.builder().message("Invalid Password")
                        .statusType(AuthenticationStatus.StatusType.ERROR).build();
            }

            hashedPass.clearHashedStringBytes();
            Arrays.fill(password, '\0');
            Arrays.fill(accountPassBytes, (byte) 0);

            conn.setAutoCommit(false);

            try {
                final int sessionId = sessionDao.createSession(conn, userId,
                        PurchaseCodeGenerator.generateSecureHexToken());

                if (sessionId == -1) {
                    LOGGER.severe(
                            String.format("Trying to create new session while logging in for %s, but failed", email));

                    return AuthenticationStatus.builder().message("Something went wrong")
                            .statusType(AuthenticationStatus.StatusType.ERROR).build();
                }

                // We're sure this exists from the condition above
                final Session session = sessionDao.getById(conn, sessionId).get();

                ApplicationConfig.getInstance().getConfig().setProperty(PropertyKey.Session.UID,
                        session.getSessionToken());
                SessionManager.getInstance().setSession(session);
                conn.commit();

                return AuthenticationStatus.builder().message("Successful sign in!")
                        .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
            } catch (SQLException | IOException err) {
                try {
                    conn.rollback();
                } catch (final SQLException err2) {
                    err.addSuppressed(err);
                }

                throw err;
            }
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, String.format("Trying to login with email %s but failed", email), err);

            return AuthenticationStatus.builder().message("Sorry. We cannot log you in at this time.")
                    .statusType(AuthenticationStatus.StatusType.ERROR).build();
        }
    }

    public static AuthenticationStatus signInFromStoredSession() {
        final String sessionToken = ApplicationConfig.getInstance().getProperty(PropertyKey.Session.UID);

        if (sessionToken == null || sessionToken.isEmpty()) {
            LOGGER.info("No stored session token. Going to authentication page.");

            return AuthenticationStatus.builder().message("Welcome!")
                    .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
        }

        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final SessionDao sessionDao = factory.getSessionDao();

        try (Connection conn = factory.getConnection()) {
            final Optional<Session> maybeSession = sessionDao.getByToken(conn, sessionToken);

            if (maybeSession.isEmpty()) {
                ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);
                LOGGER.warning("Trying to login from stored session, but it's invalid or expired.");

                return AuthenticationStatus.builder().message("Please login again.")
                        .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
            }

            final Session session = maybeSession.get();

            if (session.isExpired()) {
                sessionDao.removeByToken(conn, sessionToken);
                ApplicationConfig.getInstance().getConfig().remove(PropertyKey.Session.UID);

                return AuthenticationStatus.builder().message("Please login again.")
                        .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
            }

            SessionManager.getInstance().setSession(session);

            return AuthenticationStatus.builder().message("You have been logged in")
                    .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
        } catch (SQLException | IOException err) {
            LOGGER.warning(
                    String.format("Trying to login from stored session, but something went wrong: ", err.getMessage()));

            return AuthenticationStatus.builder().message("Please login again.")
                    .statusType(AuthenticationStatus.StatusType.ERROR).build();
        }
    }

    public static AuthenticationStatus signOut() {
        final String sessionToken = SessionManager.getInstance().getSession().getSessionToken();
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final SessionDao sessionDao = factory.getSessionDao();

        try (Connection conn = factory.getConnection()) {
            sessionDao.removeByToken(conn, sessionToken);
            ApplicationConfig.getInstance().getConfig().remove(sessionToken);
            SessionManager.getInstance().removeSession();

            return AuthenticationStatus.builder().message("You have been signed out!")
                    .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
        } catch (SQLException | IOException err) {
            LOGGER.severe(String.format("Trying to log out, but something went wrong: %s", err.getMessage()));

            return AuthenticationStatus.builder().message("Sorry. We cannot sign you out at this time.")
                    .statusType(AuthenticationStatus.StatusType.ERROR).build();
        }
    }

    public static AuthenticationStatus signUp(final String displayName, final String firstName, final String lastName,
            final String email, final char[] password) {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final UserDao userDao = factory.getUserDao();
        final AccountDao accountDao = factory.getAccountDao();

        try (Connection conn = factory.getConnection()) {
            if (userDao.isDisplayNameTaken(conn, displayName) || accountDao.getUserIdByEmail(conn, email) == -1) {
                LOGGER.warning(String.format("Trying to sign up with existing display name %s", displayName));

                return AuthenticationStatus.builder().message("Display name is already taken")
                        .statusType(AuthenticationStatus.StatusType.ERROR).build();
            }

            final Optional<HashedStringWithSalt> maybeHashedPass = Hasher.hash(password);

            if (maybeHashedPass.isEmpty()) {
                LOGGER.severe("Trying to sign up but password cant be hashed");

                return AuthenticationStatus.builder().message("Sorry. We cannot sign you up at this time.")
                        .statusType(AuthenticationStatus.StatusType.ERROR).build();
            }

            final HashedStringWithSalt hashedPass = maybeHashedPass.get();

            Arrays.fill(password, '\0');

            conn.setAutoCommit(false);

            try {
                final int userId = userDao.createUser(conn, displayName, firstName, lastName);

                if (userId == -1) {
                    LOGGER.warning(String.format("Trying to create users table for %s, but failed.", email));
                    conn.rollback();

                    return AuthenticationStatus.builder().message("Sorry. We cannot sign you up at this time.")
                            .statusType(AuthenticationStatus.StatusType.ERROR).build();
                }

                final int accountId = accountDao.createAccount(conn, userId, hashedPass.hashedStringToBase64(),
                        hashedPass.salt().toBase64(), email);

                if (accountId == -1) {
                    LOGGER.warning(String.format("Trying to create accounts table for %s, but failed.", email));
                    conn.rollback();

                    return AuthenticationStatus.builder().message("Sorry. We cannot sign you up at this time.")
                            .statusType(AuthenticationStatus.StatusType.ERROR).build();
                }

                hashedPass.clearHashedStringBytes();

                conn.commit();

                return AuthenticationStatus.builder().message("Your account has been created! Please sign in")
                        .statusType(AuthenticationStatus.StatusType.SUCCESS).build();
            } catch (SQLException | IOException err) {
                try {
                    conn.rollback();
                } catch (final SQLException err2) {
                    err.addSuppressed(err2);
                }

                throw err;
            }
        } catch (SQLException | IOException err) {
            LOGGER.severe(
                    String.format("Trying to sign up for %s, but something went wrong: %s", email, err.getMessage()));

            return AuthenticationStatus.builder().message("Sorry. We cannot sign you up at this time.")
                    .statusType(AuthenticationStatus.StatusType.ERROR).build();
        }
    }
}
