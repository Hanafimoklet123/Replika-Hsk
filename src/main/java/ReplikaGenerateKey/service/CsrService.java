package ReplikaGenerateKey.service;

import ReplikaGenerateKey.entity.KeyStoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Configuration
@Slf4j
@EnableConfigurationProperties(KeyStoreProperties.class)
@Service
public class CsrService {

    @Autowired
    private KeyStoreProperties keyStoreProperties;

    public String genCsr(String keyAlias, String subjectInfo)throws Exception{

        KeyStore keyStore = KeyStore.getInstance("JKS");

        keyStore.load(new FileInputStream(this.keyStoreProperties.getSenderKeyStore()),
                this.keyStoreProperties.getSenderPassword().toCharArray());
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, this.keyStoreProperties.getSenderPassword().toCharArray());

        KeyStore keyStore1 = KeyStore.getInstance("JKS");
        keyStore1.load(new FileInputStream(this.keyStoreProperties.getReceiverKeyStore()), this.keyStoreProperties.getReceiverPassword().toCharArray());
        String alias1 = keyStore1.aliases().nextElement();
        Certificate certificate =   keyStore1.getCertificate(alias1);
        PublicKey publicKey = certificate.getPublicKey();

        log.info("senderKeystore:{}",this.keyStoreProperties.getSenderKeyStore());
        log.info("receiverKeystore:{}",this.keyStoreProperties.getReceiverKeyStore());
        log.info("Success to create Csr");

        PKCS10CertificationRequest csr = generateCSR(new KeyPair(publicKey, privateKey), subjectInfo);
        String pemCSR = convertCSRToPEM(csr);

        return pemCSR;

    }

    private static PKCS10CertificationRequest generateCSR(KeyPair keyPair, String subjectInfo) throws OperatorCreationException {

        X500Name subject = new X500Name(subjectInfo);

        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256WithRSA");
        JcaPKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());

        return csrBuilder.build(csBuilder.build(keyPair.getPrivate()));
    }

    private static String convertCSRToPEM(PKCS10CertificationRequest csr) throws Exception {
        StringWriter stringWriter = new StringWriter();
        try (PemWriter pemWriter = new PemWriter(stringWriter)) {
            pemWriter.writeObject(new PemObject("CERTIFICATE REQUEST", csr.getEncoded()));
        }
        return stringWriter.toString();

    }
}
