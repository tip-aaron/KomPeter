/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.lib.logger.KompeterLogger;
import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.form.pointofsale.LeftPanel;
import kompeter.ui.components.form.pointofsale.LoadingPanel;
import kompeter.ui.components.form.pointofsale.RightPanel;
import kompeter.ui.system.Form;
import kompeter.ui.utils.HtmlUtils;
import kompeter.ui.workers.pointofsale.LoadProductsWorker;
import kompeter.ui.workers.pointofsale.RenderProductsWorker;
import net.miginfocom.swing.MigLayout;

public class FormPosShop extends Form implements PropertyChangeListener {
    private static final Logger LOGGER = KompeterLogger.getLogger(FormPosShop.class);
    private Cart cart;
    private LeftPanel leftPanel;
    private CartProductDisplayList productDisplayList;
    private RightPanel rightPanel;
    private JSplitPane splitPane;

    private void loadData() {
        SwingUtilities.invokeLater(() -> {
            leftPanel.getContent().removeAll();
            leftPanel.getContent().repaint();
            leftPanel.getContent().revalidate();
        });

        LoadProductsWorker.builder().cart(cart).productDisplayList(productDisplayList).container(leftPanel)
                .loadingPanel(new LoadingPanel()).build()
                .execute();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("products")) {
            RenderProductsWorker.builder()
                    .cart(cart)
                    .container(leftPanel)
                    .loadingPanel(new LoadingPanel())
                    .productDisplayList(productDisplayList)
                    .build()
                    .execute();
        }
    }

    @Override
    public boolean formBeforeClose() {
        if (cart.isEmpty()) {
            return true;
        }

        final int chosenOption = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),
                "Cart is not empty. Would you like to save the current cart's state? (If you clear the cart, this will remove the added discounts as well.)",
                "Save or Remove",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        boolean returnVal = false;

        switch (chosenOption) {
            case JOptionPane.CANCEL_OPTION:
                returnVal = false;
                break;
            case JOptionPane.YES_OPTION:
                returnVal = true;
                break;
            case JOptionPane.NO_OPTION:
                cart.clearProducts();
                returnVal = true;
                break;
        }

        return returnVal;
    }

    @Override
    public boolean formBeforeLogout() {
        if (cart.isEmpty()) {
            return true;
        }

        final int chosenOption = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(this),
                "Cart is not empty. Would you like to continue logging out?", "Log out?", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        return chosenOption == JOptionPane.YES_OPTION;
    }

    @Override
    public void formClose() {
        LOGGER.info("Closing FormPosShop...");
        leftPanel.close();
        rightPanel.close();
        productDisplayList.getPropertyChangeSupport().removePropertyChangeListener(this);
    }

    @Override
    public void formDestroy() {
        leftPanel.destroy();
        rightPanel.destroy();
    }

    @Override
    public void formInit() {
        productDisplayList = new CartProductDisplayList();
        cart = new Cart();
        leftPanel = new LeftPanel(productDisplayList);
        rightPanel = new RightPanel(cart);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        final JPanel headerPanel = new JPanel(
                new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));
        final JLabel title = new JLabel("Products");
        final JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml("Click a product card to add them to cart."));

        setLayout(new MigLayout("insets 0, flowx", "[grow, fill, center]", "[][grow, fill, top]"));

        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");

        splitPane.add(leftPanel);
        splitPane.add(rightPanel);

        headerPanel.add(title, "growx");
        headerPanel.add(subtitle, "growx");

        add(headerPanel, "growx, wrap");
        add(splitPane, "grow");

        loadData();
    }

    @Override
    public void formOpen() {
        LOGGER.info("Opening FormPosShop...");
        rightPanel.open();
        leftPanel.open();

        productDisplayList.getPropertyChangeSupport().addPropertyChangeListener(this);
    }

    @Override
    public void formRefresh() {
        loadData();
    }
}
