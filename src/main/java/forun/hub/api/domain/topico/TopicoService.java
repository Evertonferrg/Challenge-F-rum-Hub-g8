package forun.hub.api.domain.topico;

import forun.hub.api.domain.curso.CursoRepository;
import forun.hub.api.domain.usuarios.UsuarioRepository;
import forun.hub.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public Page<DadosListagemTopico> buscarPorCursoEAno(String nomeCurso, Integer ano, Pageable paginacao){
        return topicoRepository.buscarPorCursoEAno(nomeCurso, ano, paginacao)
                                .map(DadosListagemTopico::new);
    }

    @Transactional
    public Topico cadastrar(DadosCadastroTopico dados) {
        if (!usuarioRepository.existsById(dados.autorId()) || !cursoRepository.existsById(dados.cursoId())) {
            throw new ValidacaoException("Autor ou curso não encontrado para o tópico!");
        }

        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            throw new ValidacaoException("Já existe um tópico com este título e mensagem!");
        }

        var autor = usuarioRepository.getReferenceById(dados.autorId());
        var curso = cursoRepository.getReferenceById(dados.cursoId());
        var topico = new Topico(dados, autor, curso);

        return topicoRepository.save(topico);
    }


}
