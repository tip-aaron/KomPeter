/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

import java.util.logging.Logger;

import kompeter.lib.logger.KompeterLogger;

public abstract class AConfig {
    protected static Logger LOGGER = KompeterLogger.getLogger(AConfig.class);

    public abstract String getFileDirectory();

    public abstract String getFileExtension();

    public String getFileName() {
        return getClass().getSimpleName();
    }

    public String getFullPath() {
        return getFileDirectory() + getFileName() + getFileExtension();
    }
}
