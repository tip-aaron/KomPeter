package kompeter.ui.components.popup;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.products.ProductBrandDao;
import kompeter.database.dao.products.ProductCategoryDao;
import kompeter.database.dto.products.ProductBrand;
import kompeter.database.dto.products.ProductCategory;
import kompeter.ui.components.icons.SVGIconUIColor;

public abstract class FilterPopupMenu extends JPopupMenu implements ItemListener, ActionListener {

    private static void removeAllItemListeners(final FilterPopupMenu menu) {
        for (final Component component : menu.getComponents()) {
            if (component instanceof final JMenuItem item) {
                for (final ItemListener l : item.getItemListeners()) {
                    item.removeItemListener(l);
                }
            }
        }
    }

    protected Runnable listener;

    protected JButton trigger;

    public FilterPopupMenu(final Runnable listener) {
        this.listener = listener;
        trigger = new JButton(new SVGIconUIColor("filter.svg", 0.5f, "foreground.background"));

        trigger.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
        trigger.putClientProperty(FlatClientProperties.STYLE, "font:11;");

        trigger.addActionListener(this);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        show(trigger, 0, trigger.getHeight());
    }

    public abstract void populate();

    public JButton trigger() {
        return trigger;
    }

    public static class CategoryBrandFilterPopupMenu extends FilterPopupMenu {
        public final AtomicReference<ArrayList<ProductCategory>> categoryFilters;
        public final AtomicReference<ArrayList<ProductBrand>> brandFilters;

        private ArrayList<ProductCategory> categories;
        private ArrayList<ProductBrand> brands;

        public CategoryBrandFilterPopupMenu(final Runnable listener) {
            super(listener);

            categoryFilters = new AtomicReference<>(new ArrayList<>());
            brandFilters = new AtomicReference<>(new ArrayList<>());
        }

        @Override
        public void populate() {
            final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
            final ProductBrandDao productBrandDao = factory.getProductBrandDao();
            final ProductCategoryDao productCategoryDao = factory.getProductCategoryDao();

            try (Connection conn = factory.getConnection()) {
                brands = productBrandDao.getAllProductBrands(conn);
                categories = productCategoryDao.getAllProductCategories(conn);

                final ArrayList<ProductCategory> categoryFilters = this.categoryFilters.getAcquire();
                final ArrayList<ProductBrand> brandFilters = this.brandFilters.getAcquire();

                removeAllItemListeners(this);
                removeAll();

                add(new JLabel("Categories"));

                for (final ProductCategory category : categories) {
                    final JCheckBox c = new JCheckBox(category.getName());

                    if (categoryFilters.contains(category)) {
                        c.setSelected(true);
                    }

                    c.setName("category");
                    c.addItemListener(this);
                    add(c);
                }

                addSeparator();
                add(new JLabel("Brands"));

                for (final ProductBrand brand : brands) {
                    final JCheckBox c = new JCheckBox(brand.getName());

                    if (brandFilters.contains(brand)) {
                        c.setSelected(true);
                    }

                    c.setName("brand");
                    c.addItemListener(this);
                    add(c);
                }
            } catch (final SQLException | IOException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), e.getMessage(),
                            "Failed to load categories and brands.", JOptionPane.ERROR_MESSAGE);
                });
            }
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            final JCheckBox jc = (JCheckBox) e.getItemSelectable();
            final String text = jc.getText();
            final String name = jc.getName();

            final ArrayList<ProductBrand> brandFilters = this.brandFilters.getAcquire();
            final ArrayList<ProductCategory> categoryFilters = this.categoryFilters.getAcquire();

            switch (e.getStateChange()) {
                case ItemEvent.DESELECTED -> {
                    if (name.equals("brand")) {
                        final ProductBrand brand = brands.stream()
                                .filter((b) -> b.getName().equals(text)).findFirst().get();

                        brandFilters.remove(brand);
                    } else if (name.equals("category")) {
                        final ProductCategory category = categories.stream()
                                .filter((c) -> c.getName().equals(text)).findFirst().get();

                        categoryFilters.remove(category);
                    }
                }
                case ItemEvent.SELECTED -> {
                    if (name.equals("brand")) {
                        final ProductBrand brand = brands.stream()
                                .filter((b) -> b.getName().equals(text)).findFirst().get();

                        brandFilters.add(brand);
                    } else if (name.equals("category")) {
                        final ProductCategory category = categories.stream()
                                .filter((c) -> c.getName().equals(text)).findFirst().get();

                        categoryFilters.add(category);
                    }
                }
            }

            listener.run();
        }
    }
}
