package kompeter.services.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.logging.Logger;

import kompeter.database.dao.ADaoFactory;
import kompeter.database.dao.products.ProductDao;
import kompeter.database.dao.purchase_orders.PurchaseOrderDao;
import kompeter.database.dao.purchase_orders.PurchaseOrderLineDao;
import kompeter.database.dto.etc.AverageCost;
import kompeter.database.dto.etc.Supplier;
import kompeter.database.dto.purchases.PurchaseOrderLineData;
import kompeter.lib.logger.KompeterLogger;

public class Purchase {
    private final static Logger LOGGER = KompeterLogger.getLogger(Purchase.class);

    public static void addPurchase(
            final String purchaseCode,
            final Supplier supplier,
            final BigDecimal vatRate,
            final PurchaseOrderLineData[] data) {
        addPurchase(purchaseCode, Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)), supplier, vatRate, data);
    }

    public static void addPurchase(
            final String purchaseCode,
            final Timestamp purchaseDate,
            final Supplier supplier,
            final BigDecimal vatRate,
            final PurchaseOrderLineData[] data) {
        LOGGER.info("Adding a purchase order");

        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
        final PurchaseOrderDao purchaseOrderDao = factory.getPurchaseOrderDao();
        final PurchaseOrderLineDao purchaseOrderLineDao = factory.getPurchaseOrderLineDao();
        final ProductDao productDao = factory.getProductDao();

        try (Connection conn = factory.getConnection()) {
            LOGGER.info("Checking whether all lines for purchase order are valid products");
            for (final PurchaseOrderLineData d : data) {
                if (!productDao.exists(conn, d.getProductId())) {
                    throw new SQLException(String.format(
                            "Product %d doesn't exist in the database, but we are trying to add it in purchase orders",
                            d.getProductId()));
                }
            }
            LOGGER.info("All products are valid. Proceeding with the operation.");

            conn.setAutoCommit(false);

            try {
                LOGGER.info("Querying the database for purchase order insertion...");

                final int purchaseOrderId = purchaseOrderDao.createPurchaseOrder(conn, purchaseCode, purchaseDate,
                        supplier.getId(),
                        vatRate);

                if (purchaseOrderId == -1) {
                    throw new SQLException(
                            "Failure to create purchase order id");
                }

                LOGGER.info(String.format("Added purhcase order. Its id is %d", purchaseOrderId));

                for (final PurchaseOrderLineData d : data) {
                    LOGGER.info(String.format("Adding product id %d to purchase order lines", d.getProductId()));

                    d.getProductId();
                    d.getQuantity();
                    d.getUnitPrice();

                    LOGGER.info(String.format("Recalculating average cost of product %d", d.getProductId()));

                    final BigDecimal avgCost;
                    final BigDecimal avgVatRate;
                    final int countOfLines = purchaseOrderLineDao.getCountOfPurchaseOrdersOfProduct(conn,
                            d.getProductId());
                    final BigDecimal totalCost = d.getUnitPrice().multiply(new BigDecimal(d.getQuantity()));

                    if (countOfLines == -1) {
                        LOGGER.warning(
                                String.format("No existence of product %d in purchase orders yet.", d.getProductId()));

                        avgCost = totalCost;
                        avgVatRate = vatRate;
                    } else {
                        LOGGER.info(String.format("Fetching previous average cost of product %d", d.getProductId()));
                        /**
                         * (Old avg x old n + new entry) / old n + 1
                         */

                        final Optional<AverageCost> maybeAverageCost = productDao.getAvgCost(conn, d.getProductId());

                        if (maybeAverageCost.isEmpty()) {
                            throw new SQLException(String.format(
                                    "Average cost of product %d doesn't exist in database. This shouldn't happen",
                                    d.getProductId()));
                        }

                        final AverageCost averageCost = maybeAverageCost.get();

                        avgCost = averageCost.getAvgCost()
                                .multiply(new BigDecimal(countOfLines))
                                .add(totalCost)
                                .divide(new BigDecimal(countOfLines + 1));
                        avgVatRate = averageCost.getAvgVatRate()
                                .multiply(new BigDecimal(countOfLines))
                                .add(vatRate)
                                .divide(new BigDecimal(countOfLines + 1));

                        LOGGER.info(String.format("Recalculated avg cost for product %d: \n%s\n%s", d.getProductId(),
                                avgCost, avgVatRate));
                    }

                    LOGGER.info(String.format("Getting markup rate of product %d", d.getProductId()));
                    final Optional<BigDecimal> maybeMarkupRate = productDao.getMarkupRate(conn, d.getProductId());

                    if (maybeMarkupRate.isEmpty()) {
                        throw new SQLException(String.format(
                                "Markup rate of product %d doesn't exist in database. This shouldn't happen",
                                d.getProductId()));
                    }

                    final BigDecimal markupRate = maybeMarkupRate.get();
                    final BigDecimal netAvgCost = avgCost.divide(avgVatRate.add(BigDecimal.ONE));
                    final BigDecimal newPrice = netAvgCost.multiply(markupRate.add(BigDecimal.ONE));

                    LOGGER.info(String.format(
                            "Changing price of product %d to %s with a net avg cost of %s and markup rate of %s",
                            d.getProductId(), newPrice.toString(), netAvgCost.toString(), markupRate.toString()));
                    productDao.changeSellingPrice(conn, d.getProductId(), newPrice, avgCost, avgVatRate);
                    purchaseOrderLineDao.createPurchaseOrderLine(conn,
                            d.getProductId(), purchaseOrderId, d.getQuantity(), d.getUnitPrice());

                    LOGGER.info(String.format("Added product id %d to purchase order lines", d.getProductId()));
                }

                LOGGER.info(String.format("Committing all changes in addition to purchase order with id %d",
                        purchaseOrderId));
                conn.commit();
                LOGGER.info(String.format("Committing all changes in addition to purchase order with id %d",
                        purchaseOrderId));
            } catch (SQLException | IOException err) {
                try {
                    conn.rollback();
                } catch (final SQLException err2) {
                    err.addSuppressed(err2);
                }

                throw err;
            }
        } catch (SQLException | IOException err) {
            LOGGER.severe(String.format("Failed to add a purchase order: %s", err.getMessage()));
        }
    }
}
