package ReplikaGenerateKey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.antlr.v4.runtime.misc.NotNull;

@Value
@Builder
@AllArgsConstructor
public class Key {

    @NotNull
    private String algo;

    @NotNull
    private int length;
}

