package ReplikaGenerateKey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@Builder
public class SignRequest {

    @NotNull
    private String keyAlias;

    @NotNull
    private String message;

    @NotNull
    private String signAlgo;
}
