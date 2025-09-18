package com.github.ragudos.kompeter.cryptography;

import com.github.ragudos.kompeter.utilities.logger.KompeterLogger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.jetbrains.annotations.NotNull;

public final class Hasher {
    public static final Logger LOGGER = KompeterLogger.getLogger(Hasher.class);
    public static final int DEFAULT_WORK_FACTOR_STRENGTH = 65_536;
    public static final int KEY_LENGTH = 256;
    public static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static Optional<HashedStringWithSalt> hash(@NotNull final char[] password) {
        return hash(password, SaltFactory.generateSalt());
    }

    public static Optional<HashedStringWithSalt> hash(
            @NotNull final char[] password, @NotNull final Salt salt) {
        try {
            PBEKeySpec pbeKeySpec =
                    new PBEKeySpec(password, salt.value(), DEFAULT_WORK_FACTOR_STRENGTH, KEY_LENGTH);
            byte[] hashedString =
                    SecretKeyFactory.getInstance(ALGORITHM).generateSecret(pbeKeySpec).getEncoded();

            pbeKeySpec.clearPassword();

            return Optional.of(new HashedStringWithSalt(hashedString, salt));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException err) {
            LOGGER.log(Level.SEVERE, "Cannot hash string", err);
        }

        return Optional.empty();
    }
}
