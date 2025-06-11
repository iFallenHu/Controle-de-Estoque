package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.UsuarioLoginDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import br.com.techsolucoes.ControleEstoque.security.jwt.JwtService;
import br.com.techsolucoes.ControleEstoque.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void deveRetornarTokenQuandoLoginForValido() throws Exception {
        String email = "teste@email.com";
        String senha = "123456";
        String token = "jwt_token_falso";

        when(usuarioService.autenticar2(email, senha)).thenReturn(true);
        when(jwtService.generateToken(email)).thenReturn(token);

        String jsonRequest = """
                {
                    "email": "teste@email.com",
                    "senha": "123456"
                }
                """;

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"" + token + "\"}"));
    }

    @Test
    void deveRetornar401QuandoLoginForInvalido() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO("email@email.com", "senhaErrada");

        when(usuarioService.autenticar2(loginDTO.getEmail(), loginDTO.getSenha())).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Credenciais inválidas"));
    }

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO("João", "joao@email.com", "senha123", Perfil.OPERADOR);
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.OPERADOR);

        doNothing().when(usuarioService).verificarEmailDuplicado(requestDTO.getEmail());
        when(usuarioService.salvar(any(UsuarioRequestDTO.class))).thenReturn(responseDTO);

        String jsonRequest = """
                {
                    "nome": "João",
                    "email": "joao@email.com",
                    "senha": "senha123",
                    "perfil": "OPERADOR"
                }
                """;

        String expectedResponse = """
                {
                    "id": 1,
                    "nome": "João",
                    "email": "joao@email.com",
                    "perfil": "OPERADOR"
                }
                """;

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponse));
    }

}
