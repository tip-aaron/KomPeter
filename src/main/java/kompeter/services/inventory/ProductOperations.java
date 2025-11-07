package kompeter.services.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.products.ProductDao;
import kompeter.database.dto.products.Product;
import kompeter.database.dto.products.ProductBrand;
import kompeter.database.dto.products.ProductCategory;
import kompeter.lib.logger.KompeterLogger;

public class ProductOperations {
    private static final Logger LOGGER = KompeterLogger.getLogger(ProductOperations.class);

    public static void addProduct(
            final String name,
            final String description,
            final ProductCategory category,
            final ProductBrand brand,
            final String displayImage,
            final int minimumQuantity,
            final BigDecimal markupRate) {
        LOGGER.info("Adding product...");

        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final ProductDao productDao = factory.getProductDao();

        try (Connection conn = factory.getConnection()) {
            final int productId = productDao.createProduct(conn,
                    Product.builder()
                            .id(-1)
                            .markupRate(markupRate)
                            .minimumQuantity(minimumQuantity)
                            .name(name)
                            .description(description)
                            .displayImage(displayImage)
                            .category(category)
                            .brand(brand).build());

            LOGGER.info(String.format("Product %s has been created in the database with id %d", name, productId));
        } catch (SQLException | IOException err) {
            LOGGER.severe(String.format("Failed to add product %s:\n %s", name, err.getMessage()));
        }
    }
}
