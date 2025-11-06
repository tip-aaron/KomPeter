/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

import java.io.File;

import kompeter.constants.Directories;

public abstract class AFileSystemConfig extends AConfig {
    @Override
    public String getFileDirectory() {
        return Directories.CONFIG + File.separator;
    }

    public String getHeaderComment() {
        return "A configuration file for Kompeter. Refer to the documentation for more information.";
    }
}
