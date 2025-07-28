package forun.hub.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import forun.hub.api.domain.curso.Curso;
import forun.hub.api.domain.resposta.DadosCadastroResposta; // Importa seu DTO real de resposta
import forun.hub.api.domain.resposta.Resposta;
import forun.hub.api.domain.topico.*;
import forun.hub.api.domain.usuarios.Usuario;
import forun.hub.api.infra.security.SecurityTestConfig;
import jakarta.validation.Valid;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // para serializar JSON
    @Autowired
    private TopicoRepository repository;

    @Test
    @WithMockUser // mocka usuário autenticado (se estiver usando Spring Security)
    void deveCadastrarTopicoComDadosValidos() throws Exception {
        var dadosCadastro = new DadosCadastroTopico(
                "Título do tópico de teste",
                "Mensagem do tópico de teste",
                Status.NAO_RESPONDIDA,
                1L, // autorId
                1L  // cursoId
        );

        var json = objectMapper.writeValueAsString(dadosCadastro);

        mockMvc.perform(post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.titulo").value("Título do tópico de teste"));
    }

    @Test
    @WithMockUser
    void deveListarTopicos() throws Exception {
        mockMvc.perform(get("/topicos")
                        .param("size", "5")
                        .param("sort", "dataCriacao,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

//    Teste: deveCadastrarRespostaComDadosValidos

    @Test
    @WithMockUser
    void deveCadastrarRespostaComDadosValidos() throws Exception {
        // Cria dados fictícios válidos (ajuste os IDs conforme o que existe no seu banco de teste)
        var dados = new DadosCadastroResposta(
                "Minha resposta foi atualizada com sucesso!",
                12L, // topicoId já existente
                1L   // autorId já existente
        );

        var json = objectMapper.writeValueAsString(dados);

        mockMvc.perform(post("/respostas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Minha resposta foi atualizada com sucesso!"))
                .andExpect(jsonPath("$.topicoId").value(12))
                .andExpect(jsonPath("$.autorId").value(1))
                .andExpect(jsonPath("$.solucao").value(false));
    }
    // outros testes para detalhar, atualizar, excluir etc.

//    Teste: deveDetalharTopicoComRespostas

    @Test
    @WithMockUser
    void deveDetalharTopicoComRespostas() throws Exception {
        mockMvc.perform(get("/topicos/12")) // 12 = ID do tópico que já tem respostas
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.respostas").isArray())
                .andExpect(jsonPath("$.respostas[0].mensagem").exists());
    }

//    Teste: deveDeletarTopicoComSucesso

    @Test
    @WithMockUser
    void deveDeletarTopicoComSucesso() throws Exception {
        mockMvc.perform(delete("/topicos/12"))
                .andExpect(status().isNoContent());
    }


//    Rejeitar cadastro de tópico com dados inválidos
@Test
@WithMockUser
void naoDeveCadastrarTopicoComDadosInvalidos() throws Exception {
    var json = "{}"; // corpo vazio

    mockMvc.perform(post("/topicos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
            .andExpect(status().isBadRequest());
}

//    Acesso negado sem autenticação

    @Test
    void naoDevePermitirAcessoSemAutenticacao() throws Exception {

            mockMvc.perform(get("/topicos"))
                    .andExpect(status().isForbidden()); // 403

    }


//    Atualizar um tópico existente
@PutMapping("/{id}")
@Transactional
public ResponseEntity<DadosDetalhamentoTopico> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
    var topico = repository.getReferenceById(id);

    topico.atualizarInformacoes(dados); // método na entidade Topico

    return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
}


//    Buscar tópico inexistente (erro 404)

    @Test
    @WithMockUser
    void deveRetornar404ParaTopicoInexistente() throws Exception {
        mockMvc.perform(get("/topicos/9999")) // ID que não existe
                .andExpect(status().isNotFound());
    }
}
