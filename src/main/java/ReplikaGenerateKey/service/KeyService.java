package ReplikaGenerateKey.service;

import ReplikaGenerateKey.model.Keys;
import ReplikaGenerateKey.repository.KeysRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil;
import org.bouncycastle.jcajce.provider.asymmetric.util.PrimeCertaintyCalculator;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Slf4j
public class KeyService {

    private final KeysRepository keysRepository;

    public KeyService(KeysRepository keysRepository) {
        this.keysRepository = keysRepository;
    }

    @SneakyThrows
    public String getKeyAlias() {
        RSAKeyPairGenerator keyGen = new RSAKeyPairGenerator();
        keyGen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001), new SecureRandom(), 2048, PrimeCertaintyCalculator.getDefaultCertainty(2048)));
        AsymmetricCipherKeyPair pair = keyGen.generateKeyPair();

        RSAKeyParameters pub = (RSAKeyParameters) pair.getPublic();
        RSAPrivateCrtKeyParameters priv = (RSAPrivateCrtKeyParameters) pair.getPrivate();

        // As in BCRSAPrivateKey / BCRSAPublicKey
        AlgorithmIdentifier algo = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
        byte[] publicKey = KeyUtil.getEncodedSubjectPublicKeyInfo(algo, new RSAPublicKey(pub.getModulus(), pub.getExponent()));
        byte[] privateKey = KeyUtil.getEncodedPrivateKeyInfo(algo, new RSAPrivateKey(priv.getModulus(),
                priv.getPublicExponent(), priv.getExponent(), priv.getP(), priv.getQ(), priv.getDP(), priv.getDQ(),
                priv.getQInv()));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String keyAlias  = "FH-" + new String(digest.digest(publicKey), StandardCharsets.UTF_8);

        System.out.println(keyAlias + " \n check keyAlias");
        System.out.println(digest.digest(publicKey) + " \n check byte KeyAlias");

        var key = new Keys();
        key.setKeyAlias(keyAlias);
        key.setKeyPublicData(Base64.getEncoder().encodeToString(publicKey));
        key.setKeyPrivateData(Base64.getEncoder().encodeToString(privateKey));
        key.setKeyCreatetime(LocalDateTime.now());
        keysRepository.save(key);


        System.out.println(Base64.getEncoder().encodeToString(publicKey) + " \n check public");
        System.out.println(Base64.getEncoder().encodeToString(privateKey) + " \n check private ");
        return keyAlias;
    }
}
