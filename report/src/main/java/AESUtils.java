import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;


public class AESUtils {
    SecretKeySpec secretKeySpec;


    public AESUtils(String keyword) {
        generateKey(keyword);
    }

    public void generateKey(String keyword) {
        String keyStr = keyword;
        byte[] key = null;
        try {
            key = (keyStr).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            this.secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            System.out.println("Key for DES could not be generated! Keyword " + keyword + " does not work!");
            System.out.println("Exception: " + e.getStackTrace());
        }
    }

    public byte[] encrypt(byte[] data) {
        byte[] encryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec);
            encryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            System.out.println("AES Encryption does not work properly!");
            System.out.println("Exception: " + e.getStackTrace());
        }
        return encryptedData;
    }

    public byte[] decrypt(byte[] data) {
        byte[] decryptedData = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            System.out.println("AES Decryption does not work properly!");
            System.out.println("Exception: " + e.getStackTrace());
        }
        return decryptedData;
    }
}