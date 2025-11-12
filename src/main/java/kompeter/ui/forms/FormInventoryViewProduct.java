package kompeter.ui.forms;

import kompeter.ui.system.Form;
import kompeter.ui.utils.SystemForm;

@SystemForm(name = "View a Product", description = "View information about a product")
public class FormInventoryViewProduct extends Form {
    /**
     * The id of the product to be shown statistics of
     */
    private final int currentProductId = -1;

}
