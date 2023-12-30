package ReplikaGenerateKey.Controller;

import ReplikaGenerateKey.entity.KeyStoreProperties;
import ReplikaGenerateKey.entity.SignRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Map;


@Configuration
@Slf4j
@EnableConfigurationProperties(KeyStoreProperties.class)
@RestController
@RequestMapping("/sign")
public class SignController {
    @Autowired
    private KeyStoreProperties keyStoreProperties;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public String sign(@RequestBody SignRequest request) {
        // Example data to be signed
        String dataToSign = request.getMessage();
        var decode = Base64.getDecoder().decode(dataToSign);
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find("SHA-256");
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, decode);
        byte[] hashToEncrypt = digestInfo.getEncoded();


        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(this.keyStoreProperties.getSenderKeyStore()),
                this.keyStoreProperties.getSenderPassword().toCharArray());
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias,
                this.keyStoreProperties.getSenderPassword().toCharArray());


        var signature = Signature.getInstance("NONEwithRSA");
        signature.initSign(privateKey);
        signature.update(hashToEncrypt);
        var sign = signature.sign();

        log.info("Success to sign");
        var response = Base64.getEncoder().encodeToString(sign);
        return "{\n\"data\": \""+response+"\"\n}";
    }

    @PostMapping(value = "/decrypt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public String decrypt(@RequestBody Map<String, String> request) {

        KeyStore keystore1 = KeyStore.getInstance("JKS");
        keystore1.load(new FileInputStream(this.keyStoreProperties.getReceiverKeyStore()), this.keyStoreProperties.getReceiverPassword().toCharArray());
        String alias1 = keystore1.aliases().nextElement();
        Certificate certificate = keystore1.getCertificate(alias1);
        PublicKey publicKey = certificate.getPublicKey();

        var s = request.get("message");
        var decode = Base64.getDecoder().decode(s);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        var bytes = cipher.doFinal(decode);

        log.info("Success to decrypt");
        var response = Base64.getEncoder().encodeToString(bytes);
        return "{\n\"data\": \""+response+"\"\n}";
    }

    @PostMapping(value = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public String verify(@RequestBody Map<String, String> request) {

        KeyStore keystore1 = KeyStore.getInstance("JKS");
        keystore1.load(new FileInputStream(this.keyStoreProperties.getReceiverKeyStore()), this.keyStoreProperties.getReceiverPassword().toCharArray());
        String alias1 = keystore1.aliases().nextElement();
        Certificate certificate = keystore1.getCertificate(alias1);
        PublicKey publicKey = certificate.getPublicKey();

        var s = request.get("signature");
        var s1 = request.get("message");

        var signature = Base64.getDecoder().decode(s);
        var message = Base64.getDecoder().decode(s1);

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(message);

        log.info("Success to verify");
        var response = sig.verify(signature);
        return "{\n\"result\": \""+response+"\"\n}";
    }


    @PostMapping(value = "/v2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public String signv2(@RequestBody SignRequest request) {
        // Example data to be signed
        String dataToSign = request.getMessage();

        var decode = dataToSign.getBytes(StandardCharsets.UTF_8);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(this.keyStoreProperties.getSenderKeyStore()),
                this.keyStoreProperties.getSenderPassword().toCharArray());
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias,
                this.keyStoreProperties.getSenderPassword().toCharArray());


        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(decode);

        var encryptedMessage = signature.sign();

        log.info("Success to sign");
        var response = Base64.getEncoder().encodeToString(encryptedMessage);
        return "{\n\"data\": \""+response+"\"\n}";
    }
}
