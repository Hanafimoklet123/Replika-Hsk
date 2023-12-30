package ReplikaGenerateKey.Controller;

import ReplikaGenerateKey.entity.KeyRequest;
import ReplikaGenerateKey.service.KeyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping("/key")
public class KeyController {
    public final KeyService keyService;

    public KeyController(KeyService keyService) {
        this.keyService = keyService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String create(@RequestBody KeyRequest request) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException {
        String response = keyService.getKeyAlias(request.getAlgo(),request.getLength());

        return "{\n\"keyAlias\": \""+response+"\"\n}";
    }
}
