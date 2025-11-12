/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.forms;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.form.pointofsale.LeftPanel;
import kompeter.ui.components.form.pointofsale.RightPanel;
import kompeter.ui.system.Form;
import kompeter.ui.utils.HtmlUtils;
import kompeter.ui.workers.pointofsale.LoadProductsWorker;
import net.miginfocom.swing.MigLayout;

public class FormPosShop extends Form {
    private Cart cart;
    private LeftPanel leftPanel;
    private CartProductDisplayList productDisplayList;
    private RightPanel rightPanel;
    private JSplitPane splitPane;

    private void loadData() {
        SwingUtilities.invokeLater(() -> {
            leftPanel.getContentContainer().removeAll();
            leftPanel.getContentContainer().repaint();
            leftPanel.getContentContainer().revalidate();
        });

        LoadProductsWorker.builder().cart(cart).productDisplayList(productDisplayList).container(leftPanel).build()
                .execute();
    }

    @Override
    public boolean formBeforeClose() {
        return true;
    }

    @Override
    public boolean formBeforeLogout() {
        return true;
    }

    @Override
    public void formClose() {
        leftPanel.close();
        rightPanel.close();
    }

    @Override
    public void formDestroy() {
        leftPanel.destroy();
        rightPanel.destroy();
    }

    @Override
    public void formInit() {
        cart = new Cart();
        productDisplayList = new CartProductDisplayList();
        leftPanel = new LeftPanel(productDisplayList);
        rightPanel = new RightPanel(cart);
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        final JPanel headerPanel = new JPanel(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));
        final JLabel title = new JLabel("Products");
        final JLabel subtitle = new JLabel(HtmlUtils.wrapInHtml("Click a product card to add them to cart."));

        setLayout(new MigLayout("insets 0, flowx, wrap", "[grow, fill, center]"));

        splitPane.setResizeWeight(0.7);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);

        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");

        splitPane.add(leftPanel);
        splitPane.add(rightPanel);

        headerPanel.add(title, "growx");
        headerPanel.add(subtitle, "growx");

        add(headerPanel, "growx");
        add(splitPane, "grow");

        loadData();
    }

    @Override
    public void formOpen() {
        rightPanel.open();
        leftPanel.open();
    }

    @Override
    public void formRefresh() {
        loadData();
    }
}
