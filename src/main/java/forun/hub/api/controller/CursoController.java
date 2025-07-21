package forun.hub.api.controller;

import forun.hub.api.domain.curso.Curso;
import forun.hub.api.domain.curso.CursoRepository;
import forun.hub.api.domain.curso.DadosCadastroCurso;
import forun.hub.api.domain.curso.DadosListagemCurso;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("Cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemCurso> cadastrar(@RequestBody @Valid DadosCadastroCurso dados,
                                                        UriComponentsBuilder uriBuilder){

        Curso curso = new Curso(dados);

        repository.save(curso);
        var uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemCurso(curso));
    }


    public Page<DadosListagemCurso> listar(@PageableDefault(page = 0, size = 10, sort = {"nome"})Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemCurso::new);
    }
}
