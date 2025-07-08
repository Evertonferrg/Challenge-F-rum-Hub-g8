package forun.hub.api.controller;

import forun.hub.api.curso.Curso;
import forun.hub.api.curso.CursoRepository;
import forun.hub.api.topico.DadosCadastroTopico;
import forun.hub.api.topico.DadosListagemTopico;
import forun.hub.api.topico.Topico;
import forun.hub.api.topico.TopicoRepository;
import forun.hub.api.usuarios.Usuario;
import forun.hub.api.usuarios.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroTopico dados){
        Usuario autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Topico topico = new Topico(dados, autor, curso);
        repository.save(topico);

        return ResponseEntity.ok().build();

    }

    @GetMapping List<DadosListagemTopico> listar(){
        return repository.findAll().stream().map(DadosListagemTopico::new).toList();
    }

}
