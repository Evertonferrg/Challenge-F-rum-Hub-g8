package forun.hub.api.domain.topico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    public Page<DadosListagemTopico> buscarPorCursoEAno(String nomeCurso, Integer ano, Pageable paginacao){
        return topicoRepository.buscarPorCursoEAno(nomeCurso, ano, paginacao)
                                .map(DadosListagemTopico::new);
    }

}
