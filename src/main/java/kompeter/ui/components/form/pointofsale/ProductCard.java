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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.formdev.flatlaf.FlatClientProperties;

import kompeter.App;
import kompeter.database.dto.products.CartProduct;
import kompeter.loader.AssetLoader;
import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.icons.SVGIconUIColor;
import kompeter.ui.components.panels.ImagePanel;
import kompeter.ui.utils.HtmlUtils;
import kompeter.utils.NumberUtils;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.DropShadowBorder;

public class ProductCard extends JPanel implements PropertyChangeListener {
    @Getter
    private final Cart cart;
    @Getter
    private final CartProduct data;
    @Getter
    private final MouseAdapter mouseListener;
    @Getter
    private final AtomicReference<AddToCartDialog> addToCartDialog;
    private final JLabel name;
    private final JLabel price;
    private final ImagePanel thumbnail;
    private final CartProductDisplayList productDisplayList;

    public ProductCard(final Cart cart, final CartProduct data, final ImagePanel thumbnail,
            final CartProductDisplayList productDisplayList) {
        this.cart = cart;
        this.data = data;
        this.mouseListener = new ProductCardMouseListener();
        this.addToCartDialog = new AtomicReference<>(null);
        this.productDisplayList = productDisplayList;

        setLayout(new BorderLayout());

        this.thumbnail = thumbnail;

        final JPanel wrapper = new JPanel(new MigLayout("flowx, wrap, insets 0", "[grow, fill, center]"));
        name = new JLabel(HtmlUtils.wrapInHtml(String.format("<p align='center'>%s", data.getName())));
        price = new JLabel(HtmlUtils
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
        JLabel quantityLabel;
        JLabel subtitle;

        public AddToCartDialog() {
            super(App.getRootFrame(), "Add to Cart", Dialog.ModalityType.APPLICATION_MODAL);

            setLayout(new MigLayout("insets 12, flowx, wrap", "[grow, fill, center]"));

            final JLabel title = new JLabel("Add to Cart");
            subtitle = new JLabel(HtmlUtils
                    .wrapInHtml(String.format("<p align='center'>This will add %s to the cart.", data.getName())));

            title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4 primary");
            subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "muted");
            subtitle.putClientProperty(FlatClientProperties.STYLE, "font:-2;");

            quantityLabel = new JLabel(String.format("Quantity (Max %s)", data.getAvailableQuantity()));
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

                    new Timer(5000, (ev) -> {
                        data.setQuantityInHand(0);
                    }).start();
                }
            }

            dispose();
        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final int availableQty = data.getAvailableQuantity();
        final String name = data.getName();
        final BigDecimal p = data.getNetPrice();
        final String dp = data.getDisplayImage();

        // update toolbar when value of data changes
        if (evt.getPropertyName().equals("quantityInCart")) {
            SwingUtilities.invokeLater(() -> {
                setToolTipText(HtmlUtils.wrapInHtml(String.format(
                        "<p>Click this card to add <strong>%s</strong> to the cart. <br><p><em>Available:" + " %s</em>",
                        name, availableQty)));
            });
        } else if (evt.getPropertyName().equals("quantityInHand")) {
            SwingUtilities.invokeLater(() -> {
                setToolTipText(HtmlUtils.wrapInHtml(String.format(
                        "<p>Click this card to add <strong>%s</strong> to the cart. <br><p><em>Available:" + " %s</em>",
                        name, availableQty)));

                if (addToCartDialog.getAcquire() == null) {
                    return;
                }

                if (availableQty <= 0) {
                    addToCartDialog.getAcquire().dispose();
                    addToCartDialog.set(null);

                    productDisplayList.removeProduct(data);
                } else {
                    final AddToCartDialog addToCartDialog = this.addToCartDialog.getAcquire();
                    final int val = (Integer) addToCartDialog.quantitySpinner.getValue();

                    if (val < availableQty) {
                        addToCartDialog.quantitySpinner.setValue(availableQty);
                    }

                    addToCartDialog.quantityLabel
                            .setText(String.format("Quantity (Max %s)", availableQty));
                }
            });
        } else if (evt.getPropertyName().equals("name")) {
            SwingUtilities.invokeLater(() -> {
                this.name.setText(HtmlUtils.wrapInHtml(String.format("<p align='center'>%s", name)));

                if (addToCartDialog.getAcquire() == null) {
                    return;
                }

                final AddToCartDialog d = addToCartDialog.getAcquire();
                d.subtitle.setText(HtmlUtils
                        .wrapInHtml(String.format("<p align='center'>This will add %s to the cart.", name)));
            });
        } else if (evt.getPropertyName().equals("netPrice")) {
            SwingUtilities.invokeLater(() -> {
                this.price.setText(HtmlUtils
                        .wrapInHtml(String.format("<p align='center'>%s", NumberUtils.formatCurrencyPh(p))));
            });
        } else if (evt.getPropertyName().equals("displayImage")) {
            new Thread(() -> {
                final BufferedImage image = AssetLoader.loadImage(dp, true);

                SwingUtilities.invokeLater(() -> {
                    thumbnail.setImage(image, true);
                });
            }).start();
        }
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

            final AddToCartDialog d = new AddToCartDialog();

            addToCartDialog.set(d);
            d.setVisible(true);
        }
    }
}
