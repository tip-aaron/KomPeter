package kompeter.ui.workers.inventory;

import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import kompeter.services.inventory.ProductList;
import kompeter.ui.components.form.inventory.ProductsTable;
import lombok.Builder;

@Builder
public class LoadProductsWorker extends SwingWorker<Void, Void> {
    private final ProductList productList;
    private final ProductsTable productsTable;
    private final JScrollPane container;

    @Override
    protected Void doInBackground() throws Exception {
        productList.reloadProducts();
        return null;
    }

    @Override
    protected void done() {
        container.setViewportView(productsTable);
        container.repaint();
        container.revalidate();
    }

}
