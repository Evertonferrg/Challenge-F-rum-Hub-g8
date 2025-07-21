package forun.hub.api.controller;

import forun.hub.api.domain.perfis.PerfilRepository;
import forun.hub.api.domain.perfis.Perfil;

import forun.hub.api.domain.usuarios.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PerfilRepository perfilRepository;
    @Autowired
    private  UsuarioService usuarioService;



    @PostMapping
    @Transactional
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados,
                                                          UriComponentsBuilder uriBuilder) {

       Perfil perfilAssociado = perfilRepository.findById(dados.perfilId())
               .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado com o ID: " + dados.perfilId()));


       Usuario usuario = new Usuario(dados, perfilAssociado);
               usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        Page<Usuario> usuarios = usuarioRepository.findAllByAtivoTrue(paginacao);
        Page<DadosListagemUsuario> page = usuarios.map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@PathVariable Long id){
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @GetMapping("/buscar")
    public Page<DadosListagemUsuario> buscarPorCursoEAno(
            @RequestParam(required = false) String nomeCurso,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao
    ) {


        return usuarioService.buscarPorCursoEAno(nomeCurso,ano, paginacao );
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoUsuario> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoUsuario dados) {

        var usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var usuario = usuarioOptional.get();
        usuario.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var topico = usuarioRepository.getReferenceById(id);
        topico.excluir();

        return ResponseEntity.noContent().build();
    }

}
