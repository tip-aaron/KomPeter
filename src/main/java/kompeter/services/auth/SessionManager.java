/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.services.auth;

import java.beans.PropertyChangeSupport;

import org.jetbrains.annotations.NotNull;

import kompeter.database.dto.users.Session;
import lombok.Getter;

@Getter
public class SessionManager {
    private static SessionManager instance;

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }

        return instance;
    }

    private final PropertyChangeSupport propertyChangeSupport;

    private Session session;

    private SessionManager() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void removeSession() {
        propertyChangeSupport.firePropertyChange("session", session, null);
        session = null;
    }

    public void setSession(@NotNull final Session session) throws IllegalArgumentException, IllegalStateException {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null.");
        }

        if (session.isExpired()) {
            throw new IllegalArgumentException("Session is expired.");
        }

        if (this.session != null) {
            throw new IllegalStateException("Session already exists.");
        }

        this.session = session;
        propertyChangeSupport.firePropertyChange("session", null, session);
    }
}
