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

public abstract class AbstractFileSystemConfigProperties extends AbstractFileSystemConfig {
    protected final Properties properties = new Properties();

    public AbstractFileSystemConfigProperties() {
        final var path = getFullPath();

        System.out.println("Initializing configuration in file system for path: " + path);

        try {
            PropertiesIO.loadPropertiesFromFileSystem(path, properties, FileLoadLeniency.CREATE_FILE_IF_MISSING);
            System.out.println("Successfully loaded configuration from file system for path: " + path);
        } catch (final IOException e) {
            System.err.println("Failed to load properties file: " + getFileName() + "\n" + e);
        } catch (final IllegalArgumentException e) {
            System.err.println("getFileName() or getFilePath() returned null or empty string. \n" + e);
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
            System.err.println("Failed to save properties after bulk remove. Reverting changes.\n" + e);

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
            System.err.println("Failed to save properties after remove. Reverting changes. \n" + e);
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
            System.err.println("Failed to save properties after set property. Reverting changes. \n" + e);
            properties.setProperty(key, (String) prevVal);
            throw e;
        }

        return prevVal;
    }
}
