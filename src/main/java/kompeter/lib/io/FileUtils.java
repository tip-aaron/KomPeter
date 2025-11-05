/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.io;

import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;

public final class FileUtils {
    public static final void createDirectoryIfNotExists(@NotNull final String directoryPath) {
        final var directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Failed to create directory: " + directoryPath);
            }
        }
    }

    public static void createFileIfNotExists(@NotNull final String filePath) {
        final var file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                System.err.println(e);
            }
        }
    }
}
