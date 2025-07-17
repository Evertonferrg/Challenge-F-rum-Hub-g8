package forun.hub.api.domain.usuarios;

public record DadosListagemUsuario(Long id, String nome, String email, String perfil) {

    public DadosListagemUsuario (Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil().getNome());
    }
}