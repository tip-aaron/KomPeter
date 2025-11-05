/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.constants;

public final class PropertyKey {
    private PropertyKey() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final class Database {
        public static final String DB_NAME = "db.name";
        public static final String DB_PASSWORD = "db.password";
        public static final String DB_URL = "db.url";
        public static final String DB_USER = "db.user";
    }

    public static final class Metadata {
        public static final String APP_ENV = "app.env";
        public static final String APP_TITLE = "app.title";
        public static final String APP_VERSION = "app.version";
    }

    public static final class Search {
        public static final String RECENT = "search.recent.default";
        public static final String RECENT_FAVORITE = "search.recent.favorite";
    }

    public static final class Session {
        public static final String UID = "session.uid";
    }
}
