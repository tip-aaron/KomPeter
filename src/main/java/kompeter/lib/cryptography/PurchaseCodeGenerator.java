/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package kompeter.lib.cryptography;

/**
 * @author Peter M. Dela Cruz
 */
import java.security.SecureRandom;

public class PurchaseCodeGenerator {
    public static String generateSecureHexToken() {
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        // Convert the random bytes to a hexadecimal string
        final StringBuilder sb = new StringBuilder();
        for (final byte b : bytes) {

            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
