/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.helper;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import kompeter.database.dto.users.Role;

public final class Filter {
    private static final JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
    public static final double STRING_THRESHOLD_SIMILARITY = 0.7;

    public static boolean isInArray(final String str, final Role[] strArr) {
        for (final Role string : strArr) {
            if (str.equals(string.getName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInArray(final String str, final String[] strArr) {
        for (final String string : strArr) {
            if (str.equals(string)) {
                return true;
            }
        }

        return false;
    }

    public static boolean stringMatches(final String str1, final String str2) {
        return similarity.apply(str1, str2) >= STRING_THRESHOLD_SIMILARITY;
    }
}
