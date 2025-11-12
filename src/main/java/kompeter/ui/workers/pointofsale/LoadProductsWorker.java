/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.ui.workers.pointofsale;

import javax.swing.SwingWorker;

import kompeter.database.dto.products.CartProduct;
import kompeter.services.pointofsale.Cart;
import kompeter.services.pointofsale.CartProductDisplayList;
import kompeter.ui.components.form.pointofsale.LeftPanel;
import kompeter.ui.components.form.pointofsale.LoadingPanel;
import kompeter.ui.components.panels.ImagePanel;
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
        productDisplayList.reloadProducts();

        return null;
    }

    @Override
    protected void done() {
        RenderProductsWorker.builder()
                .cart(cart)
                .container(container)
                .loadingPanel(loadingPanel)
                .productDisplayList(productDisplayList)
                .build().execute();
    }

}
