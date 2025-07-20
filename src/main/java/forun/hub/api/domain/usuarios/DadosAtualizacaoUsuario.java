package forun.hub.api.domain.usuarios;

import forun.hub.api.domain.topico.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoUsuario(  @NotBlank String nome,
                                        @Email String email,
                                        @NotNull Integer ano,
                                        @NotNull Boolean ativo) {


}
