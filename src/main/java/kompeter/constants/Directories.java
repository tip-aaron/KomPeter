/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.constants;

import java.io.File;

import kompeter.lib.system.SystemInfo;

public final class Directories {
    public static final String APP_DATA;
    public static final String CONFIG;
    public static final String IMAGES;
    public static final String LOGS;

    public static final String SQLITE;

    static {
        if (SystemInfo.isLinux) {
            APP_DATA = String.format("%s%s.local%sshare%s%s", SystemInfo.USER_HOME, File.separator, File.separator,
                    File.separator, Metadata.APP_TITLE);
        } else if (SystemInfo.isWindows) {
            APP_DATA = String.format("%s%sAppData%sLocal%s%s", SystemInfo.USER_HOME, File.separator, File.separator,
                    File.separator, Metadata.APP_TITLE);
        } else if (SystemInfo.isMac) {
            APP_DATA = String.format("%s%sLibrary%sApplication Support%s%s", SystemInfo.USER_HOME, File.separator,
                    File.separator, File.separator, Metadata.APP_TITLE);
        } else {
            APP_DATA = String.format("%s%s.etc%s%s", SystemInfo.USER_HOME, File.separator, File.separator,
                    Metadata.APP_TITLE);
        }

        LOGS = String.format("%s%slogs", APP_DATA, File.separator);
        CONFIG = String.format("%s%sconfig", APP_DATA, File.separator);
        SQLITE = String.format("%s%ssqlite", APP_DATA, File.separator);
        IMAGES = String.format("%s%simages", APP_DATA, File.separator);
    }
}
