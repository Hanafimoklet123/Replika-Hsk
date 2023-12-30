package ReplikaGenerateKey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@ConfigurationProperties(prefix = "keystore")
public class KeyStoreProperties {
    private String senderKeyStore;
    private String senderPassword;
    private String senderAlias;
    private String receiverKeyStore;
    private String receiverPassword;
    private String receiverAlias;
}
