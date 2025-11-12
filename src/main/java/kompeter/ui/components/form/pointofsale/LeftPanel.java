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
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kompeter.lib.helper.Debouncer;
import kompeter.lib.logger.KompeterLogger;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.scroller.ScrollerFactory;
import kompeter.ui.components.textfields.SearchTextField;
import kompeter.ui.layout.ResponsiveLayout;
import kompeter.ui.layout.ResponsiveLayout.JustifyContent;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

@Getter
public class LeftPanel extends JPanel {
    private final JPanel content;
    private final JPanel headerContainer;
    private final SearchTextField searchTextField;
    private final Debouncer debouncer;
    private final JScrollPane scroller;
    private static final Logger LOGGER = KompeterLogger.getLogger(LeftPanel.class);

    private final CartProductDisplayList productDisplayList;

    private void onSearch(final String q) {
        debouncer.call(() -> {
            productDisplayList.setNameFilter(q);
        });
    }

    public LeftPanel(final CartProductDisplayList productDisplayList) {
        setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]", "[top][grow, fill, top]"));

        this.productDisplayList = productDisplayList;

        debouncer = new Debouncer(250);
        headerContainer = new JPanel(new MigLayout("insets 0, flowx", "[grow, fill]18px[]"));
        searchTextField = new SearchTextField(this::onSearch);
        content = new JPanel(new ResponsiveLayout(JustifyContent.START, new Dimension(190, -1), 6, 6));
        scroller = ScrollerFactory.createScrollPane(content);

        headerContainer.add(searchTextField);

        add(headerContainer, "growx");
        add(scroller, "grow");
    }

    private void reAddProductCardListeners() {
        for (final Component c : content.getComponents()) {
            if (c instanceof final ProductCard pc) {
                pc.addListeners();
            }
        }
    }

    private void removeProductCardListeners() {
        for (final Component c : content.getComponents()) {
            if (c instanceof final ProductCard pc) {
                pc.removeListeners();
            }
        }
    }

    public void close() {
        LOGGER.info("Closing left panel...");
        removeProductCardListeners();
        searchTextField.removeListeners();
        debouncer.cancel();
    }

    public void destroy() {
    }

    public void open() {
        LOGGER.info("Opening left panel...");
        searchTextField.addListeners();
        reAddProductCardListeners();
    }
}
