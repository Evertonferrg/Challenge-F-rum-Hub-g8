package forun.hub.api.usuarios;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank(message = "{nome.obrigatorio}")
        String nome,

        @NotBlank(message = "{email.obrigatorio}")
        @Email(message = "{email.invalido}")
        String email,

        @NotBlank(message = "{senha.obrigatoria}")
        @Size(min = 6, max = 20, message = "{senha.tamanho}")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,20}$",
                message = "{senha.formato}")
        String senha,
        @NotBlank(message = "{perfil.obrigatorio}")
        String perfil
        ){


}
