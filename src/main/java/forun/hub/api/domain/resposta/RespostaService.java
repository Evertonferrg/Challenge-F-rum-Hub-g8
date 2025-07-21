package forun.hub.api.domain.resposta;

import forun.hub.api.domain.topico.TopicoRepository;
import forun.hub.api.domain.usuarios.Usuario;
import forun.hub.api.domain.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;

import forun.hub.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;
    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Resposta cadastrar(DadosCadastroResposta dados) {
        if (!topicoRepository.existsById(dados.topicoId()) || !usuarioRepository.existsById(dados.autorId())) {
            throw new ValidacaoException("Tópico ou autor não encontrado.");
        }

        var topico = topicoRepository.getReferenceById(dados.topicoId());
        var autor = usuarioRepository.getReferenceById(dados.autorId());
        var resposta = new Resposta(dados.mensagem(), topico, autor);

        return respostaRepository.save(resposta);
    }

    @Transactional
    public void marcarComoSolucao(Long idResposta){
        var resposta = respostaRepository.findById(idResposta)
                .orElseThrow(() -> new ValidacaoException("Resposta não encotrada!"));

        if (resposta.getTopico().getSolucao() != null) {
            throw new ValidacaoException("Este tópico já possui uma solução marcada!");
        }

        resposta.marcarComoSolucao();
        respostaRepository.save(resposta);
    }
    @Transactional
    public Resposta atualizar(Long id, DadosAtualizacaoResposta dados) {
        var resposta = respostaRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Resposta não encontrada!"));

        // Obtenha o objeto de autenticação do contexto de segurança
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var usuarioAutenticado = (Usuario) authentication.getPrincipal();

        // Verifique se o usuário autenticado é o autor da resposta
        if (!resposta.getAutor().getId().equals(usuarioAutenticado.getId())) {
            throw new ValidacaoException("Apenas o autor da resposta pode atualizá-la.");
        }

        resposta.setMensagem(dados.mensagem()); // Ou use um método como 'atualizarInformacoes' na entidade
        return resposta;
    }

}
