package ReplikaGenerateKey.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "key")
public class Keys {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key_alias")
    private String keyAlias;

    @Column(columnDefinition = "TEXT")
    private String keyPublicData;

    @Column(columnDefinition = "TEXT")
    private String keyPrivateData;

    private LocalDateTime keyCreatetime;

    private String keyUpdateTime;

}
