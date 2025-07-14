package forun.hub.api.controller;

import forun.hub.api.curso.Curso;
import forun.hub.api.curso.CursoRepository;
import forun.hub.api.topico.*;
import forun.hub.api.usuarios.Usuario;
import forun.hub.api.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroTopico dados) {
        Usuario autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Topico topico = new Topico(dados, autor, curso);
        repository.save(topico);

        return ResponseEntity.ok().build();

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 10,
            sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao) {
        Page<DadosListagemTopico> page = repository.findAllByAtivoTrue(paginacao).map(
                DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }



    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id){
        var topicoOptional = repository.findById(id);
        if (topicoOptional.isPresent()){
            return ResponseEntity.ok(new DadosDetalhamentoTopico(topicoOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public Page<DadosListagemTopico> buscarPorCursoEAno(
            @RequestParam(required = false) String nomeCurso,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao
    ) {

       return topicoService.buscarPorCursoEAno(nomeCurso,ano, paginacao );
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoTopico dados) {
        var topico = repository.getReferenceById(dados.id());
        topico.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
       var topico = repository.getReferenceById(id);
       topico.excluir();
    }


}
