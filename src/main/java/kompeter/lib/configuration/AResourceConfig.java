/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

public abstract class AResourceConfig extends AConfig {
    @Override
    public String getFileDirectory() {
        return "";
    }

    public abstract Class<? extends AResourceConfig> getResourceClass();
}
