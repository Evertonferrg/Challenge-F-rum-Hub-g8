package forun.hub.api.domain.topico;

import forun.hub.api.domain.resposta.DadosDetalhamentoResposta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        Status status,
        String autorNome,
        String cursoNome,
        List<DadosDetalhamentoResposta> respostas
) {

    public DadosDetalhamentoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(), // Assumindo que o Topico tem um método getStatus()
                topico.getAutor().getNome(), // Assumindo que Usuario tem um método getNome()
                topico.getCurso().getNome(), // Assumindo que Curso tem um método getNome()
                topico.getRespostas() != null ?
                        topico.getRespostas().stream()
                                .map(DadosDetalhamentoResposta::new)
                                .collect(Collectors.toList()) :
                        null
        );
    }
}
