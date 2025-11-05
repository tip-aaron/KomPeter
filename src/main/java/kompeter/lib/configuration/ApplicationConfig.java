/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.configuration;

import kompeter.constants.Metadata;

public class ApplicationConfig {
    private static ApplicationConfig instance;

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            return new ApplicationConfig();
        }

        return instance;
    }

    private final FileSystemApplicationConfig config;

    private final ReadonlyApplicationConfig defaultConfig;

    private ApplicationConfig() {
        config = new FileSystemApplicationConfig();
        defaultConfig = new ReadonlyApplicationConfig();
    }

    public FileSystemApplicationConfig getConfig() {
        return config;
    }

    public ReadonlyApplicationConfig getDefaultConfig() {
        return defaultConfig;
    }

    public String getProperty(final String key) {
        var value = config.getProperty(key);

        if (value == null) {
            value = defaultConfig.getProperty(key);
        }

        return value == null ? "" : value;
    }

    public class FileSystemApplicationConfig extends AbstractFileSystemConfigProperties {
        public FileSystemApplicationConfig() {
        }

        @Override
        public String getFileName() {
            return ApplicationConfig.class.getSimpleName() + "-" + Metadata.APP_ENV;
        }
    }

    public class ReadonlyApplicationConfig extends AbstractResourceConfigProperties {
        public ReadonlyApplicationConfig() {
        }

        @Override
        public String getFileName() {
            return ApplicationConfig.class.getSimpleName() + "-" + Metadata.APP_ENV;
        }

        @Override
        public Class<? extends AbstractResourceConfig> getResourceClass() {
            return ReadonlyApplicationConfig.class;
        }
    }
}
