package forun.hub.api.controller;

import forun.hub.api.domain.resposta.DadosCadastroResposta;
import forun.hub.api.domain.resposta.DadosDetalhamentoResposta;
import forun.hub.api.domain.resposta.Resposta;
import forun.hub.api.domain.resposta.RespostaRepository;
import forun.hub.api.domain.topico.TopicoRepository;
import forun.hub.api.domain.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("Respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoResposta> cadastrar (@RequestBody @Valid DadosCadastroResposta dados,
                                                                UriComponentsBuilder uriBuilder) {

        if (!topicoRepository.existsById(dados.topicoId())) {
            return ResponseEntity.notFound().build();
        }
        if (!usuarioRepository.existsById(dados.topicoId())) {
            return ResponseEntity.notFound().build();
        }
        var topico = topicoRepository.getReferenceById(dados.topicoId());
        var autor = usuarioRepository.getReferenceById(dados.autorId());

        var resposta = new Resposta(dados.mensagem());
        respostaRepository.save(resposta);

        URI uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoResposta(resposta));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoResposta>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"})Pageable paginacao) {
        var page = respostaRepository.findAll(paginacao).map(DadosDetalhamentoResposta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoResposta> detalhar(@PathVariable Long id) {
        var resposta = respostaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
    }
}
