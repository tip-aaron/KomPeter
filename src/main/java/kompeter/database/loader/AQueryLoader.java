/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.database.loader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import kompeter.lib.logger.KompeterLogger;
import lombok.Builder;
import lombok.Data;

public abstract class AQueryLoader {
    static final Logger LOGGER = KompeterLogger.getLogger(AQueryLoader.class);

    public abstract String getDatabaseName();

    public String getQuery(final SqlQueryData data) throws FileNotFoundException, IOException {
        final String path = String.format("../sql/%s/%s/%s/%s.sql", getDatabaseName(), data.getQueryType(),
                data.getTableName(), data.getFileName());

        try (final InputStream stream = AQueryLoader.class.getResourceAsStream(path)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (NullPointerException | OutOfMemoryError err) {
            LOGGER.severe(err.getMessage());
        }

        return null;
    }

    @Data
    @Builder
    public static class SqlQueryData {
        String fileName;
        SqlQueryType queryType;
        String tableName;
    }

    public enum SqlQueryType {
        SELECT, INSERT, UPDATE, DELETE;

        @Override
        public String toString() {
            return switch (this) {
                case SELECT -> "select";
                case INSERT -> "insert";
                case UPDATE -> "update";
                case DELETE -> "delete";
            };
        }
    }
}
