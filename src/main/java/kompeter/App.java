/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import kompeter.constants.Directories;
import kompeter.database.migrator.impl.sqlite.SqliteMigrator;
import kompeter.database.seeder.impl.sqlite.SqliteSeeder;
import kompeter.lib.logger.KompeterLogger;
import kompeter.ui.frames.SplashScreen;
import kompeter.utils.FileUtils;

public class App {
    private static Logger LOGGER = KompeterLogger.getLogger(App.class);

    static SplashScreen splashScreen;

    public static void main(final String[] args) throws InterruptedException {
        KompeterLogger.setLevel(Level.ALL);

        SwingUtilities.invokeLater(() -> {
            splashScreen = new SplashScreen();
        });

        FileUtils.createDirectoryIfNotExists(Directories.CONFIG);
        FileUtils.createDirectoryIfNotExists(Directories.IMAGES);
        FileUtils.createDirectoryIfNotExists(Directories.LOGS);
        FileUtils.createDirectoryIfNotExists(Directories.SQLITE);

        setupDb();

        new JFrame().setVisible(true);
    }

    static void setupDb() {
        try {
            new SqliteMigrator().migrate();
            new SqliteSeeder().seed();
        } catch (SQLException | IOException err) {
            LOGGER.log(Level.SEVERE, "Failed to setup database", err);

            SwingUtilities.invokeLater(() -> {
                splashScreen.setVisible(false);
                JOptionPane.showMessageDialog(null,
                        "Sorry. We cannot start the application because we cannot setup the database.",
                        "Failed to Initialize", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}
