/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.cryptography;

import java.security.SecureRandom;

public final class SaltFactory {
    public static Salt generateSalt() {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] salt = new byte[16];

        secureRandom.nextBytes(salt);

        return new Salt(salt);
    }
}
