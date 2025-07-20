package forun.hub.api.domain.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoResposta(

        @NotNull
        @NotBlank
        String mensagem
) {
}
