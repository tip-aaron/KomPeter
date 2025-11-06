/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.loader;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import kompeter.lib.logger.KompeterLogger;

public final class AssetLoader {
    private static final Logger LOGGER = KompeterLogger.getLogger(AssetLoader.class);

    public static final String BASE_IMAGE_RESOURCE_PATH = "/kompeter/ui/assets/images/";

    public static BufferedImage loadFileSystemImage(final String path) throws IOException {
        try (FileInputStream fs = new FileInputStream(path)) {
            return ImageIO.read(fs);
        } catch (final FileNotFoundException err) {
            LOGGER.warning(String.format("Image file %s cannot be found", path));

            return null;
        }
    }

    public static BufferedImage loadImage(final String path, final boolean isAbs) {
        final String p = isAbs ? path : BASE_IMAGE_RESOURCE_PATH + path;

        try {
            BufferedImage resImage = loadResourceImage(p);

            if (resImage == null) {
                resImage = loadFileSystemImage(p);
            }

            if (resImage == null) {
                resImage = loadResourceImage(BASE_IMAGE_RESOURCE_PATH + "placeholder.png");
            }

            return resImage;
        } catch (final IOException err) {
            LOGGER.severe(
                    String.format("Trying to load image from %s, but something went wrong: %s", err.getMessage()));

            return null;
        }
    }

    public static BufferedImage loadResourceImage(final String path) throws IOException {
        try (InputStream is = AssetLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                return null;
            }

            return ImageIO.read(is);
        }
    }
}
