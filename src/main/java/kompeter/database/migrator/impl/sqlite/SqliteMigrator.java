package kompeter.database.migrator.impl.sqlite;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import kompeter.database.dao.ADaoFactory;
import kompeter.database.migrator.IMigrator;
import kompeter.database.migrator.MigrationQuery;
import kompeter.lib.logger.KompeterLogger;

public class SqliteMigrator implements IMigrator {
    static final Logger LOGGER = KompeterLogger.getLogger(SqliteMigrator.class);

    static final String QUERY_CREATE_MIGRATION = """
            CREATE TABLE IF NOT EXISTS migrations (
                _migration_id INTEGER PRIMARY KEY AUTOINCREMENT,
                version_number INTEGER NOT NULL CHECK(version_number > 0),
                name TEXT NOT NULL
            );
            """;

    static final String QUERY_CHECK_EXISTENCE = """
            SELECT EXISTS (
                SELECT 1 FROM migrations
                WHERE version_number = ? AND name = ?
            );
            """;

    static final String QUERY_INSERT = """
            INSERT INTO migrations (version_number, name)
            VALUES (?, ?);
            """;

    @Override
    public void migrate() throws IOException, SQLException {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final MigrationQuery[] queries = getMigrationQueries();

        try (Connection conn = factory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(QUERY_CREATE_MIGRATION)) {
            stmt.executeUpdate();
        }

        try (Connection conn = factory.getConnection();) {
            try {
                conn.setAutoCommit(false);

                for (final MigrationQuery query : queries) {
                    final PreparedStatement existsStmt = conn.prepareStatement(QUERY_CHECK_EXISTENCE);

                    existsStmt.setInt(1, query.getVersion());
                    existsStmt.setString(2, query.getName());

                    final ResultSet rs = existsStmt.executeQuery();

                    if (rs.next() && rs.getBoolean(1)) {
                        continue;
                    }

                    for (String sqlQuery : splitStatements(query.getQuery())) {
                        sqlQuery = sqlQuery.trim();

                        if (sqlQuery.isEmpty()) {
                            continue;
                        }

                        final PreparedStatement sqlQueryStmt = conn.prepareStatement(sqlQuery);
                        sqlQueryStmt.execute();
                    }

                    final PreparedStatement insertStmt = conn.prepareStatement(QUERY_INSERT);

                    insertStmt.setInt(1, query.getVersion());
                    insertStmt.setString(2, query.getName());

                    insertStmt.executeUpdate();
                }

                conn.commit();
            } catch (final SQLException err) {
                try {
                    conn.rollback();
                } catch (final SQLException err2) {
                    err.addSuppressed(err2);
                }

                throw err;
            }
        }
    }

    private String[] splitStatements(final String sql) {
        final List<String> stmts = new ArrayList<>();
        final StringBuilder current = new StringBuilder();
        String currentDelimiter = ";";

        for (String line : sql.split("\\R")) {
            line = line.trim();

            if (line.toUpperCase().startsWith("DELIMITER ")) {
                currentDelimiter = line.substring("DELIMITER ".length()).trim();

                continue;
            }

            current.append(line).append("\n");

            final String trimmed = current.toString().replaceAll("\\s+$", "");

            if (trimmed.endsWith(currentDelimiter)) {
                final String stmt = trimmed.substring(0, trimmed.length() - currentDelimiter.length()).trim();

                if (!stmts.isEmpty()) {
                    stmts.add(stmt);
                }

                current.setLength(0);
            }
        }

        if (!current.toString().trim().isEmpty()) {
            stmts.add(current.toString().trim());
        }

        return stmts.toArray(new String[stmts.size()]);
    }

    @Override
    public MigrationQuery[] getMigrationQueries() throws IOException {
        try (final ScanResult scanRes = new ClassGraph()
                .acceptModules(IMigrator.class.getModule().getName())
                .acceptPaths("META-INF/migrations")
                .scan()) {
            final List<MigrationQuery> queries = new ArrayList<>();
            final Pattern fileNamePattern = Pattern.compile("^V(\\d+)_(.+)\\.(\\w+)$");

            for (final Resource resource : scanRes.getResourcesWithExtension(".sql")) {
                final String fileName = Paths.get(resource.getPathRelativeToClasspathElement()).getFileName()
                        .toString();
                final String query = resource.getContentAsString();
                final Matcher matcher = fileNamePattern.matcher(fileName);

                if (!matcher.matches()) {
                    LOGGER.warning(String.format("Invalid migration format found: %s. Skipping...", fileName));

                    continue;
                }

                queries.add(
                        MigrationQuery.builder()
                                .version(Integer.parseInt(matcher.group(1)))
                                .name(matcher.group(2))
                                .query(query)
                                .build());
            }

            return queries.toArray(new MigrationQuery[queries.size()]);
        }
    }
}
