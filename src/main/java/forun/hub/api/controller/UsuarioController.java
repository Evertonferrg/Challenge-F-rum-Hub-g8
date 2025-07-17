package forun.hub.api.controller;

import forun.hub.api.domain.perfis.PerfilRepository;
import forun.hub.api.domain.perfis.Perfil;
import forun.hub.api.domain.usuarios.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    private PerfilRepository perfilRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
                                                          UriComponentsBuilder uriBuilder) {

       Perfil perfilAssociado = perfilRepository.findById(dados.perfilId())
               .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com o ID: " + dados.perfilId()));


       Usuario usuario = new Usuario(null, dados.nome(), dados.email(), dados.senha(), perfilAssociado);
               repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));

    }

    public Page<DadosListagemUsuario> listar (@PageableDefault(page = 0, size = 10, sort = {"nome"}) Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemUsuario::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@PathVariable Long id){
        var usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }
}
