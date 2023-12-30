package ReplikaGenerateKey.utils;

import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.util.Base64;

public class PreHashUtils {

    @SneakyThrows
    public static String hash(String message){
        var bytes = message.getBytes();
        var sha256 = MessageDigest.getInstance("SHA256");
        var digest = sha256.digest();
        return Base64.getEncoder().encodeToString(digest);
    }
}
