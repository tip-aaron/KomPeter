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
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.lib.logger.KompeterLogger;
import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.form.pointofsale.LeftPanel;
import kompeter.ui.components.form.pointofsale.LoadingPanel;
import kompeter.ui.components.form.pointofsale.RightPanel;
import kompeter.ui.system.Form;
import kompeter.ui.utils.HtmlUtils;
import kompeter.ui.workers.pointofsale.RenderProductsWorker;
import net.miginfocom.swing.MigLayout;

public class FormPosShop extends Form implements PropertyChangeListener {
    private static final Logger LOGGER = KompeterLogger.getLogger(FormPosShop.class);
    private Cart cart;
    private LeftPanel leftPanel;
    private CartProductDisplayList productDisplayList;
    private RightPanel rightPanel;
    private JSplitPane splitPane;
    private AtomicBoolean isBusy;
    private AtomicReference<Queue<RenderProductsWorker>> queue;

    private void loadData() {
        new Thread(() -> {
            productDisplayList.reloadProducts();
        }, "Reload Products").start();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("products")) {
            final RenderProductsWorker newWorker = RenderProductsWorker.builder()
                    .cart(cart)
                    .container(leftPanel)
                    .loadingPanel(new LoadingPanel())
                    .productDisplayList(productDisplayList)
                    .build();

            newWorker.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(final PropertyChangeEvent evt) {
                    if ("state".equals(evt.getPropertyName())) {
                        if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                            LOGGER.info("Products for Point of Sale has been rendered.");

                            newWorker.removePropertyChangeListener(this);
                            // acquire here and not use que below to not cause locks
                            queue.getAcquire().remove(newWorker);
                            isBusy.set(false);
                        }
                    }
                }
            });

            final Queue<RenderProductsWorker> que = queue.getAcquire();

            que.add(newWorker);

            if (isBusy.get()) {
                final RenderProductsWorker worker = que.poll();

                worker.cancel(true);
            }

            newWorker.execute();
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
        queue = new AtomicReference<>(new ArrayBlockingQueue<>(2));
        isBusy = new AtomicBoolean(false);

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
