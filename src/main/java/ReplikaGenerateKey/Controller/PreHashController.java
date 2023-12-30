package ReplikaGenerateKey.Controller;

import ReplikaGenerateKey.utils.HashUtil;
import lombok.SneakyThrows;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/hash")
public class PreHashController {

    String senderKeyStore = "src/main/resources/digitalSignature/sender_keystore.jks";
    String receiverKeyStore = "src/main/resources/digitalSignature/receiver_keystore.jks";
    String storeType = "JKS";
    String senderAlias = "senderKeyPair";
    String receiverAlias = "receiverKeyPair";
    char[] password = "stpass123".toCharArray();


    @SneakyThrows
    @PostMapping("/validate")
    public static Boolean validate(@RequestBody Map<String, String> body) {
        KeyStore keystore1 = KeyStore.getInstance("JKS");
        keystore1.load(new FileInputStream("src/main/resources/digitalSignature/receiver_keystore.jks"), "stpass123".toCharArray());
        String alias1 = keystore1.aliases().nextElement();
        Certificate certificate = keystore1.getCertificate(alias1);
        PublicKey publicKey = certificate.getPublicKey();

        String signature = body.get("signature");
        String message = body.get("message");

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] decode = Base64.getDecoder().decode(signature);
        return sig.verify(decode);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public static String create(@RequestBody Map<String, String> body) throws Exception {
        return HashUtil.hash(body.get("message"));
    }

    private static AsymmetricCipherKeyPair generateKeyPair() {
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        SecureRandom secureRandom = new SecureRandom();

        // Use RSAKeyGenerationParameters instead of KeyGenerationParameters
        RSAKeyGenerationParameters keyGenParams = new RSAKeyGenerationParameters(
                // public exponent (e value), commonly 65537
                BigInteger.valueOf(65537),
                secureRandom,
                // key size
                2048,
                // certainty (probability that the generated value is prime)
                80
        );
        generator.init(keyGenParams);
        return generator.generateKeyPair();
    }

    private static Digest getDigest() {
        // Use SHA-256 for prehashing
        return new SHA256Digest();
    }

}

