package forun.hub.api.usuarios;

public record DadosDetalhamentoUsuario(Long id, String nome, String email) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
