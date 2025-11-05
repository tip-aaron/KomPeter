/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.cryptography;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.jetbrains.annotations.NotNull;

import kompeter.utils.CharacterUtils;

public final class Hasher {
    public static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int DEFAULT_WORK_FACTOR_STRENGTH = 65_536;
    public static final int KEY_LENGTH = 256;

    public static Optional<HashedStringWithSalt> hash(@NotNull final char[] password) {
        return hash(password, SaltFactory.generateSalt());
    }

    public static Optional<HashedStringWithSalt> hash(@NotNull final char[] password, @NotNull final Salt salt) {
        try {
            final PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt.value(), DEFAULT_WORK_FACTOR_STRENGTH,
                    KEY_LENGTH);
            final byte[] hashedString = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(pbeKeySpec).getEncoded();

            pbeKeySpec.clearPassword();

            return Optional.of(new HashedStringWithSalt(hashedString, salt));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException err) {
            System.err.println("Cannot hash string. \n" + err);
        }

        return Optional.empty();
    }

    public static void main(final String[] args) {
        final var pass = "Admin@123";
        final var hashed = hash(pass.toCharArray());
        final var str = CharacterUtils.toBase64(hashed.get().hashedString());

        System.out.println("Password: " + pass + ", Hash: " + str);
        System.out.println("Salt: " + hashed.get().salt().toBase64());
    }
}
