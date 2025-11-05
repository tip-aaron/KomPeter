/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.constants;

import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

public final class StringPatterns {
    /** From ASP.Net's codebase */
    public static @NotNull final Pattern EMAIL_REGEX = Pattern
            .compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /**
     *
     *
     * <ul>
     * <li>Has a minimum of 8 characters {8,}
     * <li>At least one upper case English letter. {@code (?=.*?[A-Z])}
     * <li>At least one lower case English letter. {@code (?=.*?[a-z])}
     * <li>At least one digit. {@code (?=.*?[0-9])}
     * <li>At least one special character.{@code (?=.*? [#?!@$%^&*-])}
     * </ul>
     */
    public static @NotNull final Pattern STRONG_PASSWORD = Pattern
            .compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
}
