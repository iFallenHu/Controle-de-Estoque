package br.com.techsolucoes.ControleEstoque.controller;

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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService; // <-- mock necessário

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        //Assertions.assertEquals(1,1);
        UsuarioRequestDTO request = new UsuarioRequestDTO("João", "joao@email.com", "123456", Perfil.OPERADOR);
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.OPERADOR);

        doNothing().when(usuarioService).verificarEmailDuplicado(request.getEmail());
        when(usuarioService.salvar(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/usuário")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void deveListarUsuarios() throws Exception {
        List<UsuarioResponseDTO> usuarios = List.of(
                new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.OPERADOR),
                new UsuarioResponseDTO(2L, "Maria", "maria@email.com", Perfil.OPERADOR)
        );

        when(usuarioService.listar()).thenReturn(usuarios);

        mockMvc.perform(get("/usuário"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João"))
                .andExpect(jsonPath("$[1].email").value("maria@email.com"));
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        UsuarioResponseDTO usuario = new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.OPERADOR);

        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuário/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João"));
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        doNothing().when(usuarioService).deletar(1L);

        mockMvc.perform(delete("/usuário/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO("João Atualizado", "joao@email.com", "novaSenha", Perfil.OPERADOR);
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "João Atualizado", "joao@email.com", Perfil.OPERADOR);

        when(usuarioService.atualizar(1L, request)).thenReturn(response);

        mockMvc.perform(put("/usuário/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"));
    }
}
