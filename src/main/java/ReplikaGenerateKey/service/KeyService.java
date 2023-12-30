package ReplikaGenerateKey.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

@Service
@Slf4j
public class KeyService {

    public String getKeyAlias(String algo, int length) throws NoSuchAlgorithmException {

        // Generate a key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algo);
        keyPairGenerator.initialize(length);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        String keyAlias = "RG-9HbUUnDk2M7asnjG4yDW1VT6iQGvfhXrR5qnnBVkpdAj";  // You can set any desired key alias
        log.info("Success to create key alias:{}",keyAlias);
        return keyAlias;
    }

}
