package forun.hub.api.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDateTime;

public record DadosCadastroTopico(

        @NotBlank
        String titulo,

        @NotBlank
        String mensagem,

        @NotNull
        Status status,

        @NotNull
        Long autorId,

        @NotNull
        Long cursoId,

        @NotBlank
        String resposta) {
}
