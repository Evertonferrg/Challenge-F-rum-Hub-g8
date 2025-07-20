package forun.hub.api.domain.usuarios;

import forun.hub.api.domain.curso.Curso;
import forun.hub.api.domain.perfis.Perfil;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String senha;
    private String email;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @Column(name = "ativo")
    private Boolean ativo = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    private Integer ano;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil != null && this.perfil.getNome() != null) {
            System.out.println("Perfil carregado: " + perfil.getNome());
            return List.of(new SimpleGrantedAuthority(perfil.getNome().toUpperCase()));
        }
        return List.of();
    }


    public Usuario(DadosCadastroUsuario dados, Perfil perfil) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.senha = new BCryptPasswordEncoder().encode(dados.senha());
        this.perfil = perfil;
        this.ativo = false;

    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public void atualizarInformacoes(@Valid DadosAtualizacaoUsuario dados) {
    }

    public void excluir() {
    }
}
