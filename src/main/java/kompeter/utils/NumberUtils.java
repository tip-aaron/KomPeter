/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtils {
    public static String formatCurrency(final BigDecimal amt, final CurrencyLocale currencyLocale) {
        return NumberFormat.getCurrencyInstance(currencyLocale.value).format(amt);
    }

    public static String formatCurrencyPh(final BigDecimal amt) {
        return formatCurrency(amt, CurrencyLocale.PHILIPPINES);
    }

    public static BigDecimal valueToBigDecimal(final Object value) {
        return switch (value) {
            case final BigDecimal bd -> bd.setScale(2, RoundingMode.HALF_UP);
            case final Number n -> new BigDecimal(n.doubleValue()).setScale(2, RoundingMode.HALF_UP);
            case final String st -> new BigDecimal(st).setScale(2, RoundingMode.HALF_UP);
            case null, default -> BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        };
    }

    public static enum CurrencyLocale {
        PHILIPPINES(Locale.of("tl", "ph"));

        private final Locale value;

        private CurrencyLocale(final Locale value) {
            this.value = value;
        }
    }
}
