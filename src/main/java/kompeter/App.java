/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import kompeter.constants.Directories;
import kompeter.database.dao.ADaoFactory;
import kompeter.lib.logger.KompeterLogger;
import kompeter.services.auth.Authentication;
import kompeter.services.auth.AuthenticationStatus;
import kompeter.ui.frames.MainFrame;
import kompeter.ui.frames.SplashScreen;
import kompeter.ui.themes.KompeterLightFlatLaf;
import kompeter.utils.FileUtils;

public class App {
    private static Logger LOGGER = KompeterLogger.getLogger(App.class);

    static SplashScreen splashScreen;

    static MainFrame mainFrame;

    public static MainFrame getRootFrame() {
        return mainFrame;
    }

    public static void main(final String[] args) throws InterruptedException {
        KompeterLightFlatLaf.setup();

        SwingUtilities.invokeLater(() -> {
            splashScreen = new SplashScreen();
        });

        KompeterLogger.setLevel(Level.ALL);
        FileUtils.createDirectoryIfNotExists(Directories.CONFIG);
        FileUtils.createDirectoryIfNotExists(Directories.IMAGES);
        FileUtils.createDirectoryIfNotExists(Directories.LOGS);
        FileUtils.createDirectoryIfNotExists(Directories.SQLITE);

        if (!ADaoFactory.getDaoFactory(ADaoFactory.SQLITE).setupDb()) {
            SwingUtilities.invokeLater(() -> {
                splashScreen.dispose();
                splashScreen = null;
            });

            return;
        }

        final AuthenticationStatus status = Authentication.signInFromStoredSession();

        switch (status.getStatusType()) {
            case SUCCESS -> {
                SwingUtilities.invokeLater(() -> {
                    mainFrame = new MainFrame();

                    splashScreen.dispose();
                    splashScreen = null;
                    mainFrame.setVisible(true);

                    JOptionPane.showMessageDialog(mainFrame, status.getMessage(), "Successful login",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            }
            case ERROR -> {
                SwingUtilities.invokeLater(() -> {
                    splashScreen.dispose();
                    JOptionPane.showMessageDialog(mainFrame, status.getMessage(), "Successful login",
                            JOptionPane.ERROR_MESSAGE);
                });
                return;
            }
        }
    }
}
