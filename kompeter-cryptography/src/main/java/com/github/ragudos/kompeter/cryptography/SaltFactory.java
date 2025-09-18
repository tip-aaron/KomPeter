package com.github.ragudos.kompeter.cryptography;

import java.security.SecureRandom;

public final class SaltFactory {
    public static Salt generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];

        secureRandom.nextBytes(salt);

        return new Salt(salt);
    }
}
