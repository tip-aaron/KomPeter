package kompeter.ui.components.form.inventory;

import javax.swing.JTable;

import kompeter.services.inventory.ProductList;

public class ProductsTable extends JTable {
    private final ProductList productList;

    public ProductsTable(final ProductList productList) {
        this.productList = productList;
    }

    public void destroy() {

    }

    public void open() {

    }

    public void close() {
    }
}
