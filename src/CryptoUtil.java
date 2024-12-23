import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.*;

class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int KEY_LENGTH = 256; // 256-bit key
    private static final int IV_LENGTH = 16; // 16 bytes for AES IV

    // Generate random IV
    private static byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);
        return iv;
    }

    // Derive AES key using SHA-256 from a password
    private static SecretKey deriveKey(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hashedPassword = digest.digest(password.getBytes());
        return new SecretKeySpec(hashedPassword, ALGORITHM);
    }

    // Encrypt data
    public static String encrypt(String password, String plainText) throws Exception {
        SecretKey key = deriveKey(password);
        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

        // Combine IV and encrypted data
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    // Decrypt data
    public static String decrypt(String password, String encryptedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedText);

        // Extract IV and encrypted data
        byte[] iv = Arrays.copyOfRange(combined, 0, IV_LENGTH);
        byte[] encrypted = Arrays.copyOfRange(combined, IV_LENGTH, combined.length);

        SecretKey key = deriveKey(password);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] plainText = cipher.doFinal(encrypted);
        return new String(plainText, "UTF-8");
    }

    // Hash a password using SHA-256
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hash = null;
        try {
            hash = digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(hash);
    }
}