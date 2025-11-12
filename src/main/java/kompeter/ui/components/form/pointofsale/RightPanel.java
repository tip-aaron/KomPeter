/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.form.pointofsale;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.App;
import kompeter.database.dto.products.CartProduct;
import kompeter.lib.logger.KompeterLogger;
import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.Discount;
import kompeter.services.pointofsale.Transaction;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.components.scroller.ScrollerFactory;
import kompeter.ui.components.spinner.CurrencySpinner;
import kompeter.ui.utils.HtmlUtils;
import kompeter.utils.NumberUtils;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.DropShadowBorder;

@Getter
public class RightPanel extends JPanel implements ActionListener {
    private static final Logger LOGGER = KompeterLogger.getLogger(RightPanel.class);

    private final Cart cart;
    private final CartPanel cartPanel;

    private final JButton checkoutBtn;
    private final JButton clearCartBtn;

    public RightPanel(final Cart cart) {
        setLayout(new MigLayout("insets 4, flowx", "[grow, fill, center]", "[grow, fill, top][bottom]"));

        this.cart = cart;

        cartPanel = new CartPanel();
        clearCartBtn = new JButton("Clear Cart", new SVGIconUIColor("x.svg", 1f, "foreground.muted"));
        checkoutBtn = new JButton("Checkout", new SVGIconUIColor("check.svg", 1f, "foreground.primary"));
        clearCartBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
        checkoutBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

        clearCartBtn.setToolTipText("Remove all items in cart");
        checkoutBtn.setToolTipText("Confirm the order and proceed to checkout");

        clearCartBtn.setActionCommand("clear_cart");
        checkoutBtn.setActionCommand("checkout");

        add(cartPanel, "wrap, grow");
        add(clearCartBtn, "growx, split 2");
        add(checkoutBtn, "growx, gapx 4px");
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("clear_cart")) {
            final int res = JOptionPane.showConfirmDialog(App.getRootFrame(),
                    "Are you sure you want to clear the cart? This will remove all inserted discounts and products in the cart.");

            if (res != JOptionPane.YES_OPTION) {
                return;
            }

            LOGGER.info("Clearing cart...");

            cart.clearProducts();

            LOGGER.info("Cleared cart");
        } else if (e.getActionCommand().equals("checkout")) {
            if (cart.getTotalDiscountedNetPrice().compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(App.getRootFrame(),
                        "Sorry. Having a discounted net price <= 0 is not allowed. Either add an item to the cart or remove a discount.",
                        "Non-Positive Values is Invalid", JOptionPane.ERROR_MESSAGE);

                return;
            }

            LOGGER.info("Checking out...");

            try {
            } finally {
            }

            LOGGER.info("Checked out...");
        }
    }

    public void close() {
        LOGGER.info("Closing Right Panel...");

        clearCartBtn.removeActionListener(this);
        checkoutBtn.removeActionListener(this);

        cartPanel.close();
    }

    public void destroy() {
        cartPanel.destroy();
    }

    public void open() {
        LOGGER.info("Opening Right Panel...");
        clearCartBtn.addActionListener(this);
        checkoutBtn.addActionListener(this);
        cartPanel.open();
    }

    private class AddDiscountDialog extends JDialog implements ActionListener {
        CurrencySpinner amount;
        JTextField discountType;

        public AddDiscountDialog() {
            super(App.getRootFrame(), "Add a Discount", Dialog.ModalityType.APPLICATION_MODAL);

            setLayout(new MigLayout("insets 12, flowx, wrap", "[grow, fill, center]"));

            final JLabel title = new JLabel("Add a Discount");
            final JLabel subtitle = new JLabel(
                    HtmlUtils.wrapInHtml("This will add a discount to the overall net price."));

            final JLabel typeLabel = new JLabel("Discount Type");
            discountType = new JTextField();
            final JLabel amountLabel = new JLabel("Discount amount");
            amount = new CurrencySpinner();

            final JButton cancelButton = new JButton("Cancel", new SVGIconUIColor("x.svg", 1f, "foreground.muted"));
            final JButton confirmButton = new JButton("Confirm",
                    new SVGIconUIColor("check.svg", 1f, "foreground.primary"));

            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
            subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "font:-2;");

            discountType.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter type of discount...");

            cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            confirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            cancelButton.setActionCommand("cancel");
            confirmButton.setActionCommand("confirm");

            cancelButton.addActionListener(this);
            confirmButton.addActionListener(this);

            add(title, "wrap");
            add(subtitle, "wrap, gapy 2px");

            add(typeLabel, "gapy 8px, wrap");
            add(discountType, "gapy 2px, wrap");

            add(amountLabel, "gapy 4px, wrap");
            add(amount, "gapy 2px, wrap");

            add(cancelButton, "split 2, gapy 16px");
            add(confirmButton, "gapx 4px");

            pack();
            setLocationRelativeTo(App.getRootFrame());
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("confirm")) {
                final BigDecimal val = (BigDecimal) amount.getValue();

                if (cart.getTotalNetPrice().subtract(val).compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(App.getRootFrame(),
                            String.format(
                                    "The discount amount added will make the total net price go below %s. This"
                                            + " is not allowed",
                                    BigDecimal.ZERO.toString()),
                            "Invalid Request", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                if (val.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(App.getRootFrame(),
                            "Please only add a discount with a monetary value > 0", "Non-Positive Value is Not Allowed",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                cart.addDiscount(Discount.builder().discountType(discountType.getText()).amount(val).build());

            }

            dispose();
        }
    }

    private class CartPanel extends JPanel implements ActionListener {
        private static final Logger LOGGER = KompeterLogger.getLogger(CartPanel.class);

        private final JButton addDiscountBtn;

        private final CartListener cartListener;

        private final JPanel content;
        private final JPanel footer;
        private final JPanel header;

        private final NoResultsPanel noResultsPanel;

        private final JButton removeDiscountBtn;
        private final JScrollPane scroller;
        private final JToolBar toolBar;
        private final JLabel totalDiscountPrice;
        private final JLabel totalNetPrice;
        private final JLabel totalDiscountedNetPrice;

        private final JLabel totalPrice;
        private final JLabel totalQuantity;
        private final JLabel totalVatPrice;

        @Override
        public void updateUI() {
            super.updateUI();
            setBorder(new DropShadowBorder(new Insets(2, 4, 8, 4), 1, 16));
        }

        public CartPanel() {
            setLayout(new BorderLayout());

            final JPanel wrapper = new JPanel(
                    new MigLayout("insets 6, flowx, wrap", "[grow, fill, center]", "[][grow, fill, top][]"));
            cartListener = new CartListener();
            content = new JPanel(
                    new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));
            noResultsPanel = new NoResultsPanel();
            scroller = ScrollerFactory.createScrollPane(noResultsPanel);
            header = new JPanel(new MigLayout("insets 4, flowx, wrap", "[grow, fill, center]"));
            footer = new JPanel(new MigLayout("insets 4, flowx, wrap 2", "[left, grow][right, grow]"));
            final JLabel qtyLabel = new JLabel("Total Quantity: ");
            final JLabel netPriceLabel = new JLabel("Total Net Price: ");
            final JLabel vatPriceLabel = new JLabel(
                    String.format("Tax (%s%s): ", Transaction.VAT_RATE.multiply(new BigDecimal(100)).toString(), "%"));
            final JLabel discPriceLabel = new JLabel("Total Discounts: ");
            final JLabel totalPriceLabel = new JLabel("Total Price: ");
            final JLabel totalDiscountedNetPriceLabel = new JLabel("Discounted Net Price:");
            totalQuantity = new JLabel("--");
            totalNetPrice = new JLabel("--.--");
            totalVatPrice = new JLabel("--.--");
            totalDiscountPrice = new JLabel("--.--");
            totalPrice = new JLabel("--.--");
            totalDiscountedNetPrice = new JLabel("--.--");

            toolBar = new JToolBar();
            addDiscountBtn = new JButton("Add Discount", new SVGIconUIColor("plus.svg", 0.5f, "foreground.background"));
            removeDiscountBtn = new JButton("Remove Discount",
                    new SVGIconUIColor("minus.svg", 0.5f, "foreground.background"));

            noResultsPanel.label.setText("No items in cart yet :(");

            addDiscountBtn.setActionCommand("add_discount");
            removeDiscountBtn.setActionCommand("remove_discount");

            addDiscountBtn.setToolTipText("Add a discount");
            removeDiscountBtn.setToolTipText("Remove a discount");

            addDiscountBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
            removeDiscountBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
            addDiscountBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            removeDiscountBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
            addDiscountBtn.putClientProperty(FlatClientProperties.STYLE, "font:-3;");
            removeDiscountBtn.putClientProperty(FlatClientProperties.STYLE, "font:-3;");

            qtyLabel.setHorizontalAlignment(JLabel.LEFT);
            netPriceLabel.setHorizontalAlignment(JLabel.LEFT);
            totalDiscountedNetPriceLabel.setHorizontalAlignment(JLabel.LEFT);
            vatPriceLabel.setHorizontalAlignment(JLabel.LEFT);
            discPriceLabel.setHorizontalAlignment(JLabel.LEFT);
            totalPriceLabel.setHorizontalAlignment(JLabel.LEFT);

            totalQuantity.setHorizontalAlignment(JLabel.RIGHT);
            totalDiscountedNetPrice.setHorizontalAlignment(JLabel.RIGHT);
            totalNetPrice.setHorizontalAlignment(JLabel.RIGHT);
            totalVatPrice.setHorizontalAlignment(JLabel.RIGHT);
            totalDiscountPrice.setHorizontalAlignment(JLabel.RIGHT);
            totalPrice.setHorizontalAlignment(JLabel.RIGHT);

            qtyLabel.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            totalQuantity.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            netPriceLabel.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            totalNetPrice.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            vatPriceLabel.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            totalVatPrice.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            discPriceLabel.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            totalDiscountPrice.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            totalDiscountedNetPrice.putClientProperty(FlatClientProperties.STYLE, "font: -2;");
            totalDiscountedNetPriceLabel.putClientProperty(FlatClientProperties.STYLE, "font: -2;");

            totalPriceLabel.putClientProperty(FlatClientProperties.STYLE, "font: -1 semibold;");
            totalPrice.putClientProperty(FlatClientProperties.STYLE, "font: -1 semibold;");

            toolBar.add(addDiscountBtn);
            toolBar.addSeparator();
            toolBar.add(removeDiscountBtn);

            header.add(toolBar, "growx");

            footer.setBorder(BorderFactory.createDashedBorder(null));

            footer.add(qtyLabel);
            footer.add(totalQuantity);
            footer.add(netPriceLabel);
            footer.add(totalNetPrice);
            footer.add(vatPriceLabel);
            footer.add(totalVatPrice);
            footer.add(discPriceLabel);
            footer.add(totalDiscountPrice);
            footer.add(totalDiscountedNetPriceLabel);
            footer.add(totalDiscountedNetPrice);

            footer.add(new JSeparator(JSeparator.HORIZONTAL), "growx, span 2");

            footer.add(totalPriceLabel);
            footer.add(totalPrice);

            wrapper.add(header, "growx");
            wrapper.add(scroller, "gapy 1px, grow");
            wrapper.add(footer, "gapy 1px, growx");

            add(wrapper, BorderLayout.CENTER);
        }

        void close() {
            LOGGER.info("Closing Cart Panel...");

            addDiscountBtn.removeActionListener(this);
            removeDiscountBtn.removeActionListener(this);
            cart.getPropertyChangeSupport().removePropertyChangeListener(cartListener);

            for (final Component c : content.getComponents()) {
                if (c instanceof final CartItemCard cic) {
                    cic.removeListeners();
                }
            }
        }

        void destroy() {
        }

        void open() {
            LOGGER.info("Opening Cart Panel...");

            addDiscountBtn.addActionListener(this);
            removeDiscountBtn.addActionListener(this);
            cart.getPropertyChangeSupport().addPropertyChangeListener(cartListener);

            for (final Component c : content.getComponents()) {
                if (c instanceof final CartItemCard cic) {
                    cic.addListeners();
                }
            }
        }

        void updateTotalLabels() {
            LOGGER.info("Updating cart footer labels");

            if (cart.getTotalQuantity() == 0) {
                totalQuantity.setText("--");
                totalDiscountPrice.setText("--.--");
                totalNetPrice.setText("--.--");
                totalVatPrice.setText("--.--");
                totalDiscountedNetPrice.setText("--.--");
                totalPrice.setText("--.--");

                return;
            }

            totalNetPrice.setText(NumberUtils.formatCurrencyPh(cart.getTotalNetPrice()));
            totalPrice.setText(NumberUtils.formatCurrencyPh(cart.getTotalPrice(Transaction.VAT_RATE)));
            totalQuantity.setText(String.format("%d", cart.getTotalQuantity()));
            totalVatPrice.setText(NumberUtils.formatCurrencyPh(cart.getTotalVatPrice(Transaction.VAT_RATE)));
            totalDiscountedNetPrice.setText(NumberUtils.formatCurrencyPh(cart.getTotalDiscountedNetPrice()));
            totalDiscountPrice.setText(NumberUtils.formatCurrencyPh(cart.getTotalDiscountPrice()));
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("add_discount")) {
                if (cart.getTotalNetPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane
                            .showMessageDialog(App.getRootFrame(),
                                    String.format("Current net price is %s. Cannot add more discounts.",
                                            BigDecimal.ZERO.toString()),
                                    "Invalid Request", JOptionPane.ERROR_MESSAGE);

                    return;
                }

                new AddDiscountDialog().setVisible(true);
            } else if (e.getActionCommand().equals("remove_discount")) {
                if (cart.getDiscounts().getAcquire().size() == 0) {
                    JOptionPane
                            .showMessageDialog(App.getRootFrame(),
                                    "Please add a discount before removing one",
                                    "Invalid Request", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                new RemoveDiscountDialog().setVisible(true);
            }
        }

        class CartListener implements PropertyChangeListener {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                SwingUtilities.invokeLater(() -> {
                    LOGGER.info(String.format("Recevied property change event: %s", evt));

                    if (evt.getPropertyName().equals("discounts")) {
                        updateTotalLabels();
                    }

                    if (evt.getPropertyName().equals("products")
                            && evt.getOldValue() instanceof final ArrayList<?> oldList
                            && evt.getNewValue() instanceof final ArrayList<?> list) {
                        if (list.isEmpty()) {
                            LOGGER.info("Cart is empty.");

                            for (final Component c : content.getComponents()) {
                                if (c instanceof final CartItemCard cic) {
                                    cic.removeListeners();
                                }
                            }

                            cart.clearDiscounts();
                            content.removeAll();
                            updateTotalLabels();

                            scroller.setViewportView(noResultsPanel);
                            repaint();
                            revalidate();

                            // added a product
                        } else if (oldList.size() < list.size()) {
                            if (oldList.isEmpty()) {
                                scroller.setViewportView(content);

                                scroller.repaint();
                                scroller.revalidate();
                            }

                            final CartProduct product = (CartProduct) list.getLast();

                            updateTotalLabels();

                            content.add(new CartItemCard(cart, product, () -> updateTotalLabels()), "growx");
                            repaint();
                            revalidate();

                            LOGGER.info(String.format("Added new product to cart: %s\nNew List size: %d", product,
                                    list.size()));
                        } else if (oldList.size() > list.size()) {
                            final ArrayList<CartItemCard> toBeRemoved = new ArrayList<>();

                            for (final Component c : content.getComponents()) {
                                if (c instanceof final CartItemCard cic) {
                                    final int indexOfRemovedProduct = list.indexOf(cic.getData());

                                    if (indexOfRemovedProduct == -1) {
                                        toBeRemoved.add(cic);
                                    }
                                }
                            }

                            if (toBeRemoved.isEmpty()) {
                                LOGGER.info(
                                        String.format(
                                                "Component to be removed doesn't exist in the UI, but something was"
                                                        + " removed from the cart. \n" + "===\n" + "Old List: %s\n"
                                                        + "===\n\n" + "===\n" + "New List: %s\n" + "===\n",
                                                oldList, list));

                                return;
                            }

                            for (final CartItemCard cic : toBeRemoved) {
                                cic.removeListeners();
                                content.remove(cic);
                            }

                            repaint();
                            revalidate();

                            LOGGER.info(String.format("Removed product from cart: %s\nNew List size: %d", toBeRemoved,
                                    list.size()));
                        }
                    }
                });
            }
        }
    }

    private class RemoveDiscountDialog extends JDialog implements ActionListener {
        private final ArrayList<Discount> toBeRemoved;

        public RemoveDiscountDialog() {
            super(App.getRootFrame(), "Remove a Discount", Dialog.ModalityType.APPLICATION_MODAL);

            toBeRemoved = new ArrayList<>();

            setLayout(new MigLayout("insets 12, flowx, wrap", "[grow, fill, center]"));

            final JLabel title = new JLabel("Remove Discount/s");
            final JLabel subtitle = new JLabel(
                    HtmlUtils.wrapInHtml("This will remove discount/s from the overall net price."));
            final JLabel discountLabel = new JLabel("Discount/s");
            final JPanel discountList = new JPanel(new MigLayout("insets 0, flowx, wrap 2", "[left]4px[left]"));
            final JScrollPane scroller = ScrollerFactory.createScrollPane(discountList);
            final JButton cancelButton = new JButton("Cancel", new SVGIconUIColor("x.svg", 1f, "foreground.muted"));
            final JButton confirmButton = new JButton("Confirm",
                    new SVGIconUIColor("check.svg", 1f, "foreground.primary"));

            int i = 1;

            for (final Discount discount : cart.getDiscounts().getAcquire()) {
                final String name = discount.getDiscountType().isEmpty()
                        ? String.format("Discount %d", i)
                        : discount.getDiscountType();

                final JCheckBox checkbox = new JCheckBox(name);
                final JLabel amount = new JLabel(NumberUtils.formatCurrencyPh(discount.getAmount()));

                checkbox.addItemListener((evt) -> {
                    switch (evt.getStateChange()) {
                        case ItemEvent.SELECTED -> {
                            toBeRemoved.add(discount);
                        }
                        case ItemEvent.DESELECTED -> {
                            toBeRemoved.remove(discount);
                        }
                    }
                });

                discountList.add(checkbox);
                discountList.add(amount);

                i++;
            }

            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
            subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "font:-2;");

            cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            confirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            cancelButton.setActionCommand("cancel");
            confirmButton.setActionCommand("confirm");

            cancelButton.addActionListener(this);
            confirmButton.addActionListener(this);

            add(title, "wrap");
            add(subtitle, "wrap, gapy 2px");

            add(discountLabel, "gapy 8px, wrap");
            add(scroller, "gapy 2px, wrap");

            add(cancelButton, "split 2, gapy 16px");
            add(confirmButton, "gapx 4px");

            pack();
            setLocationRelativeTo(App.getRootFrame());
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("cancel")) {
                dispose();
            } else if (e.getActionCommand().equals("confirm")) {
                cart.removeDiscounts(toBeRemoved);
                dispose();
            }
        }
    }
}
