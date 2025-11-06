/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import kompeter.lib.logger.KompeterLogger;

public final class FileUtils {
    static final Logger LOGGER = KompeterLogger.getLogger(FileUtils.class);

    public static final Path copyMove(final String sourcePath, final String targetPath) throws IOException {
        final Path s = Paths.get(sourcePath);
        final Path t = Paths.get(targetPath);

        final Path res = Files.copy(s, t.resolve(s.getFileName()), StandardCopyOption.REPLACE_EXISTING);

        LOGGER.fine(String.format("Copied file %s to %s", s.toString(), t.toString()));

        return res;
    }

    public static final void createDirectoryIfNotExists(@NotNull final String directoryPath) {
        final Path path = Paths.get(directoryPath);

        try {
            if (Files.exists(path)) {
                return;
            }

            Files.createDirectory(path, null);
            LOGGER.fine(String.format("Created directory %s", path.toString()));
        } catch (final SecurityException | UnsupportedOperationException | IOException err) {
            LOGGER.severe(err.getMessage());
        }
    }

    public static void createFileIfNotExists(@NotNull final String filePath) {
        final Path path = Paths.get(filePath);

        try {
            if (Files.exists(path)) {
                return;
            }

            Files.createFile(path, null);
            LOGGER.fine(String.format("Created file %s", path.toString()));
        } catch (final SecurityException | UnsupportedOperationException | IOException err) {
            LOGGER.severe(err.getMessage());
        }
    }
}
