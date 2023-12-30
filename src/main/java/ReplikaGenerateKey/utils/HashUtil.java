package ReplikaGenerateKey.utils;

import lombok.SneakyThrows;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {

    @SneakyThrows
    public static String hash(String message) {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Change this to UTF-16 if needed
        md.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();

        return Base64.getEncoder().encodeToString(digest);
    }
}

