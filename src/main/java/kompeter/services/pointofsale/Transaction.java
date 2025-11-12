/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.services.pointofsale;

import java.math.BigDecimal;

import kompeter.database.dao.ADaoFactory;

public final class Transaction {
    public static final BigDecimal VAT_RATE = new BigDecimal("0.12");

    public static void createTransaction(final Cart cart) {
        final ADaoFactory factory = ADaoFactory.getDaoFactory(ADaoFactory.SQLITE);
    }
}
