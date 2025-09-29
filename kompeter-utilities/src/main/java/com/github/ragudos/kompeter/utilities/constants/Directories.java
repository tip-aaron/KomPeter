package com.github.ragudos.kompeter.utilities.constants;

import com.github.ragudos.kompeter.utilities.platform.SystemInfo;
import java.io.File;

public final class Directories {
    public static final String LOGS_DIRECTORY;
    public static final String CONFIG_DIRECTORY;
    public static final String SQLITE_DIRECTORY;

    public static final String SQLITE_DIRECTORY;


    public static final String APP_DATA_DIRECTORY;

    /**
     * The directory where the application is installed. This is the directory where the application
     * stores its data.
     */
    public static final String APP_DIRECTORY;

    static {
        if (SystemInfo.isWindows) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "AppData";
            APP_DIRECTORY =
                    APP_DATA_DIRECTORY + File.separator + "Local" + File.separator + Metadata.APP_TITLE;
        } else if (SystemInfo.isLinux || SystemInfo.isMac) {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME;
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + "." + Metadata.APP_TITLE;
        } else {
            APP_DATA_DIRECTORY = SystemInfo.USER_HOME + File.separator + "etc";
            APP_DIRECTORY = APP_DATA_DIRECTORY + File.separator + Metadata.APP_TITLE;
        }


        SQLITE_DIRECTORY = APP_DIRECTORY + File.separator + "sqlite";
        LOGS_DIRECTORY = APP_DIRECTORY + File.separator + "logs";
        CONFIG_DIRECTORY = APP_DIRECTORY + File.separator + "config";
    }

    private Directories() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
