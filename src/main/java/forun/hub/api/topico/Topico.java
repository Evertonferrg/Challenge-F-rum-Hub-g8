package forun.hub.api.topico;

import forun.hub.api.curso.Curso;
import forun.hub.api.resposta.Resposta;
import forun.hub.api.usuarios.Usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensagem;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;
    @ManyToOne
    private Curso curso;
    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();

    private Boolean ativo;

    public Topico(DadosCadastroTopico dados, Usuario autor, Curso curso) {
        this.ativo = true;
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.status = dados.status();
        this.autor = autor;
        this.curso = curso;

    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoTopico dados) {
       if (dados.titulo() != null) {
           this.titulo = dados.titulo();
       }

       if (dados.mensagem() != null){
        this.mensagem = dados.mensagem();
       }

       if (dados.status() != null){
        this.status = dados.status();
       }

    }

    public void excluir() {
        this.ativo = false;
    }
}
