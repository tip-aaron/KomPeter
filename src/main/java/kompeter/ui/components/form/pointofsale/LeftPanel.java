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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.lib.helper.Debouncer;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.components.scroller.ScrollerFactory;
import kompeter.ui.layout.ResponsiveLayout;
import kompeter.ui.layout.ResponsiveLayout.JustifyContent;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

@Getter
public class LeftPanel extends JPanel implements DocumentListener {
    private final JPanel content;
    private final JPanel contentContainer;
    private final JPanel headerContainer;
    private final JTextField searchTextField;
    private final Debouncer debouncer;

    private final CartProductDisplayList productDisplayList;

    @Override
    public void changedUpdate(final DocumentEvent e) {

    }

    @Override
    public void insertUpdate(final DocumentEvent e) {
        search();
    }

    @Override
    public void removeUpdate(final DocumentEvent e) {
        search();
    }

    public LeftPanel(final CartProductDisplayList productDisplayList) {
        setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));

        this.productDisplayList = productDisplayList;

        debouncer = new Debouncer(250);
        headerContainer = new JPanel(new MigLayout("insets 0, flowx", "[grow, fill]18px[]"));
        searchTextField = new JTextField();
        content = new JPanel(new ResponsiveLayout(JustifyContent.START, new Dimension(150, -1), 12, 12));
        contentContainer = new JPanel(new MigLayout("insets 0 0 0 4, flowx, wrap", "[grow, fill, center]"));
        final JScrollPane scroller = ScrollerFactory.createScrollPane(contentContainer);

        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.5f, "TextField.placeholderForeground"));
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products...");
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.setToolTipText("Search an item by name");
        contentContainer.add(content);

        headerContainer.add(searchTextField);

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
        searchTextField.getDocument().removeDocumentListener(this);
        debouncer.cancel();
    }

    public void destroy() {
    }

    private void search() {
        debouncer.call(() -> {
            productDisplayList.setNameFilter(searchTextField.getText());
        });
    }

    public void open() {
        searchTextField.getDocument().addDocumentListener(this);
        search();
        reAddProductCardListeners();
    }
}
