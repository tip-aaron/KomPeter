/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.workers.pointofsale;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import kompeter.database.dto.products.CartProduct;
import kompeter.loader.AssetLoader;
import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.form.pointofsale.LeftPanel;
import kompeter.ui.components.form.pointofsale.LoadingPanel;
import kompeter.ui.components.form.pointofsale.NoResultsPanel;
import kompeter.ui.components.form.pointofsale.ProductCard;
import kompeter.ui.components.panels.ImagePanel;
import kompeter.ui.components.panels.ImagePanel.ScaleMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
class WorkerData {
    final CartProduct cartProduct;
    final ImagePanel image;
}

@Builder
public class LoadProductsWorker extends SwingWorker<Void, WorkerData> {
    private final Cart cart;
    private final LeftPanel container;
    private final LoadingPanel loadingPanel;
    private final CartProductDisplayList productDisplayList;

    @Override
    protected Void doInBackground() throws Exception {
        SwingUtilities.invokeLater(() -> {
            container.getContent().removeAll();
            container.getContent().add(loadingPanel);
            container.getContent().repaint();
            container.getContent().revalidate();
        });

        productDisplayList.reloadProducts();

        final ArrayList<CartProduct> cartProducts = productDisplayList.getProducts().getAcquire();

        for (final CartProduct cartProduct : cartProducts) {
            String imagePath = cartProduct.getDisplayImage();

            if (imagePath == null || imagePath.isEmpty()) {
                imagePath = "/kompeter/ui/assets/images/placeholder.png";
            }

            final BufferedImage image = AssetLoader.loadImage(imagePath, true);
            final ImagePanel imagePanel = new ImagePanel(image, true);

            SwingUtilities.invokeLater(() -> {
                imagePanel.setMinimumSize(new Dimension(150, 150));
                imagePanel.setMaximumSize(new Dimension(150, 150));
                imagePanel.setScaleMode(ScaleMode.CONTAIN);
            });

            final WorkerData workerData = WorkerData.builder().cartProduct(cartProduct).image(imagePanel).build();

            publish(workerData);
        }

        return null;
    }

    @Override
    protected void done() {
        container.getContent().remove(loadingPanel);

        if (productDisplayList.getProducts().getAcquire().size() == 0) {
            container.getContent().add(new NoResultsPanel());
        }

        container.repaint();
        container.revalidate();
    }

    @Override
    protected void process(final List<WorkerData> chunks) {
        container.getContent().remove(loadingPanel);

        for (final WorkerData workerData : chunks) {
            final ProductCard card = new ProductCard(cart, workerData.getCartProduct(), workerData.getImage());

            container.getContent().add(card);
        }

        container.getContent().add(loadingPanel);
        container.getContent().repaint();
        container.getContent().revalidate();
    }
}
