/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

import java.io.IOException;
import java.util.Properties;

import kompeter.lib.io.FileLoadLeniency;
import kompeter.lib.io.PropertiesIO;

public abstract class AResourceConfigProperties extends AResourceConfig {
    protected Properties properties;

    public AResourceConfigProperties() {
        final var classNameString = getResourceClass().getName();

        System.out.println("Initializing configuration in resource for path: " + getFileDirectory() + getFileName()
                + " for class: " + classNameString);

        try {
            properties = PropertiesIO.loadProperties(getResourceClass(), getFileName(), getFileDirectory(),
                    FileLoadLeniency.MANDATORY);

            System.out.println("Successfully loaded configuration from resource for path: " + getFileDirectory()
                    + getFileName() + " for class: " + classNameString);
        } catch (final IOException e) {
            System.err.println("Failed to load properties file: " + getFileName() + "\n" + e);
        } catch (final IllegalArgumentException e) {
            System.err.println("getFileName() or getFilePath() returned null or empty string. \n" + e);
        }
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
}
