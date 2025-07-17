package forun.hub.api.domain.resposta;

import java.time.LocalDateTime;


public record DadosDetalhamentoResposta(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        boolean solucao,
        Long topicoId,
        Long autorId,
        String nomeAutor
) {

    public DadosDetalhamentoResposta(Resposta resposta) {
        this(resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getSolucao(),
                resposta.getTopico() != null ? resposta.getTopico().getId() : null,
                resposta.getAutor() != null ? resposta.getAutor().getId() : null,
                resposta.getAutor() != null ? resposta.getAutor().getNome() : null);
    }
}
