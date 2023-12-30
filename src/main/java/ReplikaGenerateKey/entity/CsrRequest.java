package ReplikaGenerateKey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@Builder
public class CsrRequest {

    @NotNull
    private String keyAlias;

    @NotNull
    private String subjectInfo;
}
