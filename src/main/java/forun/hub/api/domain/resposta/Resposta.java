package forun.hub.api.domain.resposta;

import forun.hub.api.domain.topico.Topico;
import forun.hub.api.domain.usuarios.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Table(name = "respostas")
@Entity(name = "Resposta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;

    private LocalDateTime dataCriacao;
    private Boolean solucao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;


    public Resposta(String mensagem) {
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;

    }

    public void marcarComoSolucao(){
        this.solucao = true;
    }

    public Resposta(String mensagem, Topico topico, Usuario autor){
        this.mensagem = mensagem;
        this.dataCriacao = LocalDateTime.now();
        this.solucao = false;
        this.topico = topico;
        this.autor = autor;

    }
}
