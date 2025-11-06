/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.icons;

import java.awt.Color;

import javax.swing.UIManager;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.ColorFunctions;

import kompeter.App;

public class SVGIconUIColor extends FlatSVGIcon {
    public static final String ICONS_BASE_PATH = App.class.getPackageName().replace('.', '/') + "/ui/assets/icons/";
    private float alpha;

    private String colorKey;

    public SVGIconUIColor(final String name, final float scale, final String colorKey) {
        this(name, scale, colorKey, 1f);
    }

    public SVGIconUIColor(final String name, final float scale, final String colorKey, final float alpha) {
        super(ICONS_BASE_PATH + name, scale);
        this.colorKey = colorKey;
        this.alpha = alpha;
        setColorFilter(new ColorFilter(color -> {
            final Color uiColor = UIManager.getColor(getColorKey());

            if (uiColor != null) {
                return getAlpha() == 1 ? uiColor : ColorFunctions.fade(uiColor, getAlpha());
            }
            return color;
        }));
    }

    public float getAlpha() {
        return alpha;
    }

    public String getColorKey() {
        return colorKey;
    }

    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }

    public void setColorKey(final String colorKey) {
        this.colorKey = colorKey;
    }
}
