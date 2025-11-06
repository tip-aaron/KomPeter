/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import kompeter.lib.io.FileLoadLeniency;
import kompeter.lib.io.PropertiesIO;

public abstract class AFileSystemConfigProperties extends AFileSystemConfig {
    protected final Properties properties = new Properties();

    public AFileSystemConfigProperties() {
        final var path = getFullPath();

        LOGGER.info(String.format("Initializing configuration in file system for path: %s", path));

        try {
            PropertiesIO.loadPropertiesFromFileSystem(path, properties, FileLoadLeniency.CREATE_FILE_IF_MISSING);
            LOGGER.info(String.format("Successfully loaded configuration from file system for path: %s", path));
        } catch (final IOException e) {
            LOGGER.warning(String.format("Failed to load properties file at %s: %s", getFileName(), e.getMessage()));
        } catch (final IllegalArgumentException e) {
            LOGGER.warning(
                    String.format("getFileName() or getFilePath() returned null or empty string: %s", e.getMessage()));
        }
    }

    public final synchronized Object[] bulkRemove(final Object[] keys) throws FileNotFoundException, IOException {
        final var prevVals = new Object[keys.length];

        for (var i = 0; i < keys.length; i++) {
            prevVals[i] = properties.remove(keys[i]);
        }

        try {
            PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
        } catch (final Exception e) {
            LOGGER.severe("Failed to save properties after bulk remove. Reverting changes.\n" + e);

            for (var i = 0; i < keys.length; i++) {
                properties.setProperty((String) keys[i], (String) prevVals[i]);
            }

            throw e;
        }

        return prevVals;
    }

    @Override
    public final String getFileExtension() {
        return ".properties";
    }

    public final synchronized String getProperty(final String key) {
        return properties.getProperty(key);
    }

    public final synchronized String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public final synchronized Object remove(final Object key) throws FileNotFoundException, IOException {
        final var prevVal = properties.remove(key);

        try {
            PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
        } catch (final Exception e) {
            LOGGER.severe("Failed to save properties after remove. Reverting changes. \n" + e);
            properties.setProperty((String) key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }

    public final synchronized Object setProperty(final String key, final String value)
            throws FileNotFoundException, IOException {
        final var prevVal = properties.setProperty(key, value);

        try {
            PropertiesIO.savePropertiesInFileSystem(properties, getFullPath(), getHeaderComment());
        } catch (final Exception e) {
            LOGGER.severe("Failed to save properties after set property. Reverting changes. \n" + e);
            properties.setProperty(key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }
}
