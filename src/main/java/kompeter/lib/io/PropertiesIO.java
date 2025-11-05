/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.jetbrains.annotations.NotNull;

public class PropertiesIO {
    public static final String PROPERTIES_FILE_EXTENSION = ".properties";

    public static final void loadProperties(@NotNull final Class<?> clazz, @NotNull final Properties properties,
            @NotNull final String name) throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", FileLoadLeniency.MANDATORY);
    }

    public static final void loadProperties(@NotNull final Class<?> clazz, @NotNull final Properties properties,
            @NotNull final String name, final FileLoadLeniency leniency)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, "", leniency);
    }

    public static final void loadProperties(@NotNull final Class<?> clazz, @NotNull final Properties properties,
            @NotNull final String name, @NotNull final String path)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        loadProperties(clazz, properties, name, path, FileLoadLeniency.MANDATORY);
    }

    public static final void loadProperties(@NotNull final Class<?> clazz, @NotNull final Properties properties,
            @NotNull final String name, @NotNull final String path, final FileLoadLeniency leniency)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Properties file name cannot be null or empty");
        }

        if (path == null) {
            throw new IllegalArgumentException("Properties file path cannot be null");
        }

        final var filePath = path + name + PROPERTIES_FILE_EXTENSION;

        try (var inputStream = clazz.getResourceAsStream(filePath)) {
            if (properties != null) {
                properties.load(inputStream);
            } else {
                switch (leniency) {
                    case MANDATORY -> {
                        throw new FileNotFoundException("Properties file not found: " + filePath);
                    }
                    case LOG_MISSING -> {
                        System.err.println("Properties file not found: " + filePath);
                    }
                    case CREATE_FILE_IF_MISSING -> {
                        // DO nothing since we cannot write to a resource file
                        // as we don't know the path of the clazz on runtime.
                    }
                    case ALLOW_MISSING -> {
                        System.out.println("Properties file not found: " + filePath);
                    }
                }
            }
        } catch (final NullPointerException e) {
            // DO nothing
        }
    }

    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz, @NotNull final String name)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        final var properties = new Properties();

        loadProperties(clazz, properties, name);

        return properties;
    }

    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz, @NotNull final String name,
            @NotNull final String path) throws IllegalArgumentException, FileNotFoundException, IOException {
        final var properties = new Properties();

        loadProperties(clazz, properties, name, path);

        return properties;
    }

    public static final @NotNull Properties loadProperties(@NotNull final Class<?> clazz, @NotNull final String name,
            @NotNull final String path, final FileLoadLeniency leniency)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        final var properties = new Properties();

        loadProperties(clazz, properties, name, path, leniency);

        return properties;
    }

    public static final Properties loadPropertiesFromFileSystem(@NotNull final String filePath)
            throws FileNotFoundException, IOException {
        final var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties);
        return properties;
    }

    public static final Properties loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final FileLoadLeniency leniency) throws FileNotFoundException, IOException {
        final var properties = new Properties();
        loadPropertiesFromFileSystem(filePath, properties, leniency);
        return properties;
    }

    public static final void loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final Properties properties) throws FileNotFoundException, IOException {
        loadPropertiesFromFileSystem(filePath, properties, FileLoadLeniency.MANDATORY);
    }

    public static final void loadPropertiesFromFileSystem(@NotNull final String filePath,
            @NotNull final Properties properties, @NotNull final FileLoadLeniency leniency)
            throws FileNotFoundException, IOException {
        try (var inputStream = new FileInputStream(filePath)) {
            properties.load(inputStream);
        } catch (final FileNotFoundException e) {
            switch (leniency) {
                case MANDATORY -> {
                    throw new FileNotFoundException("Properties file not found: " + filePath);
                }
                case LOG_MISSING -> {
                    System.err.println("Properties file not found: " + filePath);
                }
                case CREATE_FILE_IF_MISSING -> {
                    new File(filePath).createNewFile();
                }
                case ALLOW_MISSING -> {
                    System.out.println("Properties file not found: " + filePath);
                }
            }
        }
    }

    public static final void savePropertiesInFileSystem(@NotNull final Properties properties,
            @NotNull final String filePath) throws IllegalArgumentException, FileNotFoundException, IOException {
        savePropertiesInFileSystem(properties, filePath, null);
    }

    public static final void savePropertiesInFileSystem(@NotNull final Properties properties,
            @NotNull final String filePath, @NotNull final String headerComment)
            throws IllegalArgumentException, FileNotFoundException, IOException {
        String comment = "";

        if (headerComment != null && !headerComment.isEmpty()) {
            comment = headerComment + "\n" + comment;
        }

        try (var outputStream = new FileOutputStream(filePath)) {
            properties.store(outputStream, comment);
        } catch (final SecurityException e) {
            System.err.println("Denied access to file: " + filePath);
        } catch (final ClassCastException e) {
            System.err.println(e);
        }
    }

    private PropertiesIO() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
