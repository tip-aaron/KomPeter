/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.components.form.pointofsale;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.App;
import kompeter.database.dto.products.CartProduct;
import kompeter.services.pointofsale.Cart;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.components.panels.ImagePanel;
import kompeter.ui.utils.HtmlUtils;
import kompeter.utils.NumberUtils;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.DropShadowBorder;

@Getter
public class ProductCard extends JPanel implements PropertyChangeListener {
    private final Cart cart;
    private final CartProduct data;
    private final MouseAdapter mouseListener;

    public ProductCard(final Cart cart, final CartProduct data, final ImagePanel thumbnail) {
        this.cart = cart;
        this.data = data;
        this.mouseListener = new ProductCardMouseListener();

        setLayout(new BorderLayout());

        final JPanel wrapper = new JPanel(new MigLayout("flowx, wrap, insets 0", "[grow, fill, center]"));
        final JLabel name = new JLabel(HtmlUtils.wrapInHtml(String.format("<p align='center'>%s", data.getName())));
        final JLabel price = new JLabel(HtmlUtils
                .wrapInHtml(String.format("<p align='center'>%s", NumberUtils.formatCurrencyPh(data.getNetPrice()))));

        name.putClientProperty(FlatClientProperties.STYLE, "font: bold;");
        name.setHorizontalAlignment(JLabel.CENTER);

        price.setHorizontalAlignment(JLabel.CENTER);
        price.putClientProperty(FlatClientProperties.STYLE, "font:-1;");

        wrapper.add(thumbnail);
        wrapper.add(name, "growx, gapy 4px");
        wrapper.add(price, "growx, gapy 2px");

        add(wrapper);

        setToolTipText(HtmlUtils.wrapInHtml(String.format(
                "<p>Click this card to add <strong>%s</strong> to the cart. <br><p><em>Available:" + " %s</em>",
                data.getName(), data.getAvailableQuantity())));

        // for initialization, since this won't be added on creation of this card,
        // remove just in case
        removeListeners();
        addListeners();
    }

    public void removeListeners() {
        removeMouseListener(mouseListener);
        data.getPropertyChangeSupport().removePropertyChangeListener(this);
    }

    public void addListeners() {
        addMouseListener(mouseListener);
        data.getPropertyChangeSupport().addPropertyChangeListener(this);

    }

    @Override
    public void updateUI() {
        super.updateUI();

        setBorder(new DropShadowBorder(new Insets(2, 4, 8, 4), 1, 16));
    }

    class AddToCartDialog extends JDialog implements ActionListener {
        final JSpinner quantitySpinner;

        public AddToCartDialog() {
            super(App.getRootFrame(), "Add to Cart", Dialog.ModalityType.APPLICATION_MODAL);

            setLayout(new MigLayout("insets 12, flowx, wrap", "[grow, fill, center]"));

            final JLabel title = new JLabel("Add to Cart");
            final JLabel subtitle = new JLabel(HtmlUtils
                    .wrapInHtml(String.format("<p align='center'>This will add %s to the cart.", data.getName())));

            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
            subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "font:-2;");

            final JLabel quantityLabel = new JLabel(String.format("Quantity (Max %s)", data.getAvailableQuantity()));
            quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, data.getAvailableQuantity(), 1));

            final JButton cancelButton = new JButton("Cancel", new SVGIconUIColor("x.svg", 1f, "foreground.muted"));
            final JButton confirmButton = new JButton("Confirm",
                    new SVGIconUIColor("check.svg", 1f, "foreground.primary"));

            cancelButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            confirmButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "primary");

            cancelButton.setActionCommand("cancel");
            confirmButton.setActionCommand("confirm");

            cancelButton.addActionListener(this);
            confirmButton.addActionListener(this);

            add(title, "wrap");
            add(subtitle, "wrap, gapy 2px");

            add(quantityLabel, "gapy 4px");
            add(quantitySpinner, "push, gapy 2px");

            add(cancelButton, "split 2, gapy 16px");
            add(confirmButton, "gapx 4px");

            pack();
            setLocationRelativeTo(App.getRootFrame());
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getActionCommand().equals("confirm")) {
                if (cart.exists(data.getId())) {
                    cart.addQty(data.getId(), (Integer) quantitySpinner.getValue());
                } else {
                    data.setQuantityInCart((Integer) quantitySpinner.getValue());
                    cart.addProduct(data);
                }
            }

            dispose();
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> {
            // update toolbar when value of data changes
            if (evt.getPropertyName().equals("quantityInCart")) {
                setToolTipText(HtmlUtils.wrapInHtml(String.format(
                        "<p>Click this card to add <strong>%s</strong> to the cart. <br><p><em>Available:" + " %s</em>",
                        data.getName(), data.getAvailableQuantity())));

            }
        });
    }

    class ProductCardMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            if (data.getAvailableQuantity() == 0) {
                JOptionPane.showMessageDialog(App.getRootFrame(),
                        String.format("All available quantities for %s are already in the cart.", data.getName()),
                        "Invalid Request", JOptionPane.ERROR_MESSAGE);

                return;
            }

            new AddToCartDialog().setVisible(true);
        }
    }
}
