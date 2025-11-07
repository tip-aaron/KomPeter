/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.form.pointofsale;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kompeter.services.pointofsale.ProductDisplayList;
import kompeter.ui.components.scroller.ScrollerFactory;
import kompeter.ui.layout.ResponsiveLayout;
import kompeter.ui.layout.ResponsiveLayout.JustifyContent;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

@Getter
public class LeftPanel extends JPanel {
    private final JPanel content;
    private final JPanel contentContainer;
    private final JPanel headerContainer;

    private final ProductDisplayList productDisplayList;

    public LeftPanel(final ProductDisplayList productDisplayList) {
        setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));

        this.productDisplayList = productDisplayList;

        headerContainer = new JPanel();
        content = new JPanel(new ResponsiveLayout(JustifyContent.START, new Dimension(150, -1), 12, 12));
        contentContainer = new JPanel(new MigLayout("insets 0 0 0 4, flowx, wrap", "[grow, fill, center]"));
        final JScrollPane scroller = ScrollerFactory.createScrollPane(contentContainer);

        contentContainer.add(content);

        add(headerContainer, "growx");
        add(scroller, "grow");
    }

    private void reAddProductCardListeners() {
        for (final Component c : content.getComponents()) {
            if (c instanceof final ProductCard pc) {
                pc.addMouseListener(pc.getMouseListener());
            }
        }
    }

    private void removeProductCardListeners() {
        for (final Component c : content.getComponents()) {
            if (c instanceof final ProductCard pc) {
                pc.removeMouseListener(pc.getMouseListener());
            }
        }
    }

    public void close() {
        removeProductCardListeners();
    }

    public void destroy() {
    }

    public void open() {
        reAddProductCardListeners();
    }
}
