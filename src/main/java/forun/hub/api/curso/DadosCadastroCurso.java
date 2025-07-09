package forun.hub.api.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroCurso(

        @NotBlank
        String nome,
        @NotNull
        String categoria
) {
}
