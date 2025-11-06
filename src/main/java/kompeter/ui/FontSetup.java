/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ScanResult;
import kompeter.lib.logger.KompeterLogger;

public class FontSetup {
    private static final Logger LOGGER = KompeterLogger.getLogger(FontSetup.class);

    public static final void setup() {
        try (ScanResult scanResult = new ClassGraph().acceptPackages("kompeter/ui/assets/fonts").acceptPaths("fonts")
                .scan()) {
            for (final Resource res : scanResult.getResourcesWithExtension("ttf")) {
                try (InputStream is = res.open()) {
                    final Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                    final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(font);

                    LOGGER.info("Successfully registered font: " + font.getFamily());
                } catch (final Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to register font", e);
                }
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to scan for fonts", e);
        }
    }
}
