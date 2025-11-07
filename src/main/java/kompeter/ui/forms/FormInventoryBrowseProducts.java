/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.forms;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.lib.helper.Debouncer;
import kompeter.services.inventory.ProductList;
import kompeter.ui.components.form.inventory.ProductsTable;
import kompeter.ui.components.form.pointofsale.LoadingPanel;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.components.scroller.ScrollerFactory;
import kompeter.ui.system.Form;
import kompeter.ui.utils.HtmlUtils;
import kompeter.ui.utils.SystemForm;
import kompeter.ui.workers.inventory.LoadProductsWorker;
import net.miginfocom.swing.MigLayout;

@SystemForm(name = "Inventory Browse Products", description = "Shows all products", tags = { "inventory" })
public class FormInventoryBrowseProducts extends Form implements DocumentListener {
    private ProductsTable productsTable;
    private ProductList productList;
    private JScrollPane scroller;
    private JTextField searchTextField;
    private LoadingPanel loadingPanel;

    private Debouncer debouncer;

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

    private void search() {
        debouncer.call(() -> {
            productList.setNameFilter(searchTextField.getText());
        });
    }

    @Override
    public void formInit() {
        productList = new ProductList();
        productsTable = new ProductsTable(productList);
        scroller = ScrollerFactory.createScrollPane(productsTable);
        searchTextField = new JTextField();
        debouncer = new Debouncer(250);

        setLayout(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));

        final JLabel title = new JLabel("Browse Products");
        final JLabel description = new JLabel(HtmlUtils
                .escapeHtml(
                        "Browse all products (Highlight/Right-click products to select them for editing and deletion.)"));

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");

        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new SVGIconUIColor("search.svg", 0.5f, "TextField.placeholderForeground"));
        searchTextField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search products...");
        searchTextField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        searchTextField.setToolTipText("Search a product by name");

        add(title);
        add(description);
        add(searchTextField, "gapy 16px");
        add(scroller, "grow, gapy 4px");

        loadData();
    }

    private void loadData() {
        SwingUtilities.invokeLater(() -> {
            scroller.setViewportView(loadingPanel);
            scroller.repaint();
            scroller.revalidate();
        });

        LoadProductsWorker
                .builder()
                .container(scroller)
                .productList(productList)
                .productsTable(productsTable);
    }

    @Override
    public void formRefresh() {
        loadData();
    }

    @Override
    public void formOpen() {
        searchTextField.getDocument().addDocumentListener(this);
        productsTable.open();
    }

    @Override
    public void formClose() {
        searchTextField.getDocument().removeDocumentListener(this);
        productsTable.close();
    }

    @Override
    public void formDestroy() {
        productsTable.destroy();
    }
}
