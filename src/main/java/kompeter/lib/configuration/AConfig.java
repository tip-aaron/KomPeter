/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

public abstract class AConfig {
    public abstract String getFileDirectory();

    public abstract String getFileExtension();

    public String getFileName() {
        return getClass().getSimpleName();
    }

    public String getFullPath() {
        return getFileDirectory() + getFileName() + getFileExtension();
    }
}
