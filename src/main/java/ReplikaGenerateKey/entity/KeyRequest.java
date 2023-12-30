package ReplikaGenerateKey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@Builder
public class KeyRequest {

    @NotNull
    private String algo;

    @NotNull
    private int length;

    public Key map(){
        return Key.builder().algo(algo).length(length).build();
    }
}
