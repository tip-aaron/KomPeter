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
            container.getContentContainer().add(loadingPanel);
            container.getContentContainer().repaint();
            container.getContentContainer().revalidate();
        });

        productDisplayList.reloadProducts();

        final ArrayList<CartProduct> cartProducts = productDisplayList.getProducts().getAcquire();

        for (final CartProduct cartProduct : cartProducts) {
            String imagePath = cartProduct.getDisplayImage();

            if (imagePath.isEmpty()) {
                imagePath = "/kompeter/ui/assets/ui/placeholder.png";
            }

            final BufferedImage image = AssetLoader.loadImage(imagePath, true);
            final ImagePanel imagePanel = new ImagePanel(image, true);

            imagePanel.setMinimumSize(new Dimension(125, 125));
            imagePanel.setMaximumSize(new Dimension(125, 125));
            imagePanel.setScaleMode(ScaleMode.CONTAIN);

            publish(WorkerData.builder().cartProduct(cartProduct).image(imagePanel).build());
        }

        return null;
    }

    @Override
    protected void done() {
        container.getContentContainer().remove(loadingPanel);

        if (productDisplayList.getProducts().getAcquire().size() == 0) {
            container.getContentContainer().add(new NoResultsPanel());
        }
    }

    @Override
    protected void process(final List<WorkerData> chunks) {
        container.getContentContainer().remove(loadingPanel);

        for (final WorkerData workerData : chunks) {
            container.getContent().add(new ProductCard(cart, workerData.getCartProduct(), workerData.getImage()));
        }

        container.getContentContainer().add(loadingPanel);
        container.getContentContainer().repaint();
        container.getContentContainer().revalidate();
    }
}
