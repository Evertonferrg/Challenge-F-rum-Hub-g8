package forun.hub.api.domain.topico;

import forun.hub.api.domain.curso.CursoRepository;
import forun.hub.api.domain.resposta.DadosCadastroResposta;
import forun.hub.api.domain.resposta.Resposta;
import forun.hub.api.domain.resposta.RespostaRepository;
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

    @Autowired // <-- NOVO: Adicione o RespostaRepository aqui
    private RespostaRepository respostaRepository;

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
    @Transactional
    public Resposta cadastrarResposta(Long topicoId, DadosCadastroResposta dados) {
        // 1. Validar se o tópico existe
        var topico = topicoRepository.findById(topicoId)
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado para adicionar resposta."));

        // 2. Validar se o autor da resposta existe
        var autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new ValidacaoException("Autor da resposta não encontrado."));

        // 3. Criar a nova resposta
        var novaResposta = new Resposta(dados.mensagem(), topico, autor);

        // 4. Salvar a resposta
        return respostaRepository.save(novaResposta);
    }
}


