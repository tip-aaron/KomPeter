/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.constants;

import java.io.IOException;
import java.util.Properties;

import kompeter.lib.io.PropertiesIO;

public final class Metadata {
    private static final Properties properties = new Properties();

    public static final String APP_ENV;
    public static final String APP_TITLE;
    public static final String APP_VERSION;

    static {
        try {
            PropertiesIO.loadProperties(Metadata.class, properties, "metadata");
        } catch (final IllegalArgumentException e) {
            // DO nothing
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load metadata.properties", e);
        }

        APP_TITLE = properties.getProperty(PropertyKey.Metadata.APP_TITLE);
        APP_VERSION = properties.getProperty(PropertyKey.Metadata.APP_VERSION);
        APP_ENV = properties.getProperty(PropertyKey.Metadata.APP_ENV);
    }

    private Metadata() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
