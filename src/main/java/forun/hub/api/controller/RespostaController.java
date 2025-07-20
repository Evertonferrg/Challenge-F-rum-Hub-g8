package forun.hub.api.controller;

import forun.hub.api.domain.resposta.*;
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
@RequestMapping("respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoResposta> cadastrar(@RequestBody @Valid DadosCadastroResposta dados,
                                                               UriComponentsBuilder uriBuilder) {

        if (!topicoRepository.existsById(dados.topicoId()) || !usuarioRepository.existsById(dados.autorId())) {
            return ResponseEntity.notFound().build();
        }

        var topico = topicoRepository.getReferenceById(dados.topicoId());
        var autor = usuarioRepository.getReferenceById(dados.autorId());

        // Linha que chama o construtor que você adicionou
        var resposta = new Resposta(dados.mensagem(), topico, autor);
        respostaRepository.save(resposta);

        var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoResposta(resposta));
    }


    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoResposta>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        var page = respostaRepository.findAll(paginacao).map(DadosDetalhamentoResposta::new);
        return ResponseEntity.ok(page);
    }

 
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoResposta> detalhar(@PathVariable Long id) {
        // Usa findById e Optional para um comportamento seguro e correto
        var respostaOptional = respostaRepository.findById(id);
        if (respostaOptional.isPresent()) {
            return ResponseEntity.ok(new DadosDetalhamentoResposta(respostaOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoResposta> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoResposta dados) {

        if (!respostaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        var resposta = respostaRepository.getReferenceById(id);
        resposta.setMensagem(dados.mensagem());

        return ResponseEntity.ok(new DadosDetalhamentoResposta(resposta));
    }

    // DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!respostaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        respostaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
