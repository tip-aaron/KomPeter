/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.themes;

import com.formdev.flatlaf.FlatLightLaf;

public final class KompeterLightFlatLaf extends FlatLightLaf {
    public static final String NAME = "KompeterLightFlatLaf";

    public static void installLafInfo() {
        installLafInfo(NAME, KompeterLightFlatLaf.class);
    }

    public static boolean setup() {
        return setup(new KompeterLightFlatLaf());
    }

    public KompeterLightFlatLaf() {
    }

    @Override
    public String getName() {
        return NAME;
    }
}
