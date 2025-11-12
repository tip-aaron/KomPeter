/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.form.pointofsale;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.App;
import kompeter.database.dto.products.CartProduct;
import kompeter.lib.logger.KompeterLogger;
import kompeter.services.pointofsale.Cart;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.utils.HtmlUtils;
import kompeter.utils.NumberUtils;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class CartItemCard extends JPanel implements ActionListener, PropertyChangeListener {
    private static final Logger LOGGER = KompeterLogger.getLogger(CartItemCard.class);

    private final Cart cart;
    @Getter
    private final CartProduct data;

    private final JButton decBtn;
    private final SVGIconUIColor decIcon;
    private final JButton incBtn;
    private final JLabel qty;

    private final JPanel qtyPanel;
    private final SVGIconUIColor trashIcon;

    private final Runnable updateCartLabels;
    private final JLabel netPrice;

    public CartItemCard(final Cart cart, final CartProduct data, final Runnable updateCartLabels) {
        this.cart = cart;
        this.data = data;

        this.updateCartLabels = updateCartLabels;

        setLayout(new MigLayout("insets 0, flowx", "[grow, fill, center]"));

        final JLabel name = new JLabel(HtmlUtils.wrapInHtml(data.getName()));
        netPrice = new JLabel(
                HtmlUtils.wrapInHtml(
                        String.format("Net price: %s", NumberUtils.formatCurrencyPh(data.getTotalNetPrice()))));
        qtyPanel = new JPanel(new MigLayout("insets 2, flowx, gapx 4, al center center", "[left][center][right]"));
        qty = new JLabel(String.format("%d", data.getQuantityInCart()));
        decIcon = new SVGIconUIColor("minus.svg", 0.75f, "foreground.background");
        trashIcon = new SVGIconUIColor("trash.svg", 0.75f, "foreground.background");
        decBtn = new JButton(data.getQuantityInCart() == 1 ? trashIcon : decIcon);
        incBtn = new JButton(new SVGIconUIColor("plus.svg", 0.75f, "foreground.background"));

        netPrice.putClientProperty(FlatClientProperties.STYLE, "foreground:$TextField.placeholderForeground;font:-1;");

        qtyPanel.putClientProperty(FlatClientProperties.STYLE, "arc:999;background:tint($Panel.background,25%);");
        decBtn.putClientProperty(FlatClientProperties.STYLE, "arc:999;");
        incBtn.putClientProperty(FlatClientProperties.STYLE, "arc:999;");

        decBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        incBtn.putClientProperty(FlatClientProperties.STYLE_CLASS, "ghost");
        decBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);
        incBtn.putClientProperty(FlatClientProperties.BUTTON_TYPE_BORDERLESS, true);

        decBtn.setToolTipText(String.format("Deccrement quantity of %s or remove it", data.getName()));
        incBtn.setToolTipText(String.format("Increment quantity of %s", data.getName()));

        decBtn.setActionCommand("decrement");
        incBtn.setActionCommand("increment");

        qtyPanel.add(decBtn);
        qtyPanel.add(qty);
        qtyPanel.add(incBtn);

        add(name, "wrap, growx");
        add(netPrice, "split 2, pushx, growx");
        add(qtyPanel, "gapleft 8px");

        removeListeners();
        addListeners();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equals("increment")) {
            if (data.getAvailableQuantity() == 0) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(App.getRootFrame(),
                            String.format("Cannot add more of %s to the cart.", data.getName()), "Insufficient Stock",
                            JOptionPane.ERROR_MESSAGE);
                });

                return;
            }

            data.setQuantityInCart(data.getQuantityInCart() + 1);
        } else if (e.getActionCommand().equals("decrement")) {
            if (data.getQuantityInCart() == 1) {
                cart.removeProduct(data);

                return;
            }

            data.setQuantityInCart(data.getQuantityInCart() - 1);
        }
    }

    public void addListeners() {
        decBtn.addActionListener(this);
        incBtn.addActionListener(this);

        data.getPropertyChangeSupport().addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            if (evt.getPropertyName().equals("quantityInCart") && evt.getNewValue() instanceof final Integer newQty
                    && evt.getOldValue() instanceof final Integer oldQty) {
                qty.setText(String.format("%d", newQty));
                netPrice.setText(HtmlUtils
                        .wrapInHtml(
                                String.format("Net price: %s", NumberUtils.formatCurrencyPh(data.getTotalNetPrice()))));
                LOGGER.info(
                        String.format("Changed quantity of %s in cart from %d to %d", data.getName(), oldQty, newQty));

                if (newQty == 1) {
                    decBtn.setIcon(trashIcon);
                } else if (decBtn.getIcon() != decIcon) {
                    decBtn.setIcon(decIcon);
                }

                updateCartLabels.run();
            }
        });
    }

    public void removeListeners() {
        decBtn.removeActionListener(this);
        incBtn.removeActionListener(this);

        data.getPropertyChangeSupport().removePropertyChangeListener(this);
    }
}
