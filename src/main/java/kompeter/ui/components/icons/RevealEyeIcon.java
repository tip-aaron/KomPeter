/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.icons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

import javax.swing.AbstractButton;
import javax.swing.UIManager;

import org.jetbrains.annotations.NotNull;

import com.formdev.flatlaf.util.AnimatedIcon;
import com.formdev.flatlaf.util.UIScale;

public class RevealEyeIcon implements AnimatedIcon {
    private final @NotNull SVGIconUIColor icon;
    private final int space;

    public RevealEyeIcon() {
        this(new SVGIconUIColor("eye.svg", 1f, "TextField.placeholderForeground"), 5);
    }

    public RevealEyeIcon(@NotNull final SVGIconUIColor icon, final int space) {
        this.icon = icon;
        this.space = space;
    }

    private void drawLine(final Graphics2D g2, final Shape shape, final Color color, final float size) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(UIScale.scale(size), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(shape);
    }

    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public float getValue(final Component c) {
        return ((AbstractButton) c).isSelected() ? 0 : 1;
    }

    @Override
    public void paintIconAnimated(final Component c, final Graphics g, final int x, final int y,
            final float animatedValue) {
        final Graphics2D g2 = (Graphics2D) g.create();
        final int s = UIScale.scale(space);

        icon.paintIcon(c, g2, x, y);

        if (animatedValue > 0) {
            final float startX = x + s;
            final float startY = y + getIconHeight() - s;

            final float endX = x + getIconWidth() - s;
            final float endY = y + s;

            final Shape shape = new Line2D.Float(startX, startY, startX + (endX - startX) * animatedValue,
                    startY + (endY - startY) * animatedValue);

            drawLine(g2, shape, UIManager.getColor(icon.getColorKey()), 1.5f);
        }

        g2.dispose();
    }
}
