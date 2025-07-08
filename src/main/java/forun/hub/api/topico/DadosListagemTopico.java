package forun.hub.api.topico;

import java.time.LocalDateTime;

public record DadosListagemTopico(String titulo, String mensagem, LocalDateTime dataCriacao, String autor, String estado,
                                  String curso, String resposta){

    public DadosListagemTopico(Topico topico){
        this(topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao(),topico.getAutor().getNome(), topico.getStatus().name(),topico.getCurso().getNome(), topico.getResposta());
    }
}
