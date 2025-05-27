package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.UsuarioRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioLoginDTO;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

//    @MockBean
//    private JwtUtil jwtUtil;

//    @Test
//    public void deveRetornarTokenQuandoAutenticado() throws Exception {
//        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO("joao@empresa.com", "123456");
//
//        Mockito.when(usuarioService.autenticar(loginDTO.getEmail(), loginDTO.getSenha()))
//                .thenReturn(true);
//        Mockito.when(jwtUtil.generateToken(loginDTO.getEmail()))
//                .thenReturn("fake-jwt-token");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"email\": \"joao@empresa.com\", \"senha\": \"123456\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"token\": \"fake-jwt-token\"}"));
//    }

    @Test
    public void deveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        UsuarioLoginDTO loginDTO = new UsuarioLoginDTO("joao@empresa.com", "senhaErrada");

        Mockito.when(usuarioService.autenticar(loginDTO.getEmail(), loginDTO.getSenha()))
                .thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"joao@empresa.com\", \"senha\": \"senhaErrada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"error\": \"Credenciais inválidas\"}"));
    }


//    @Test
//    public void deveCadastrarUsuarioComSucesso() throws Exception {
//        UsuarioRequestDTO usuarioDTO = new UsuarioRequestDTO();
//        usuarioDTO.setNome("João");
//        usuarioDTO.setEmail("joao@empresa.com");
//        usuarioDTO.setSenha("123456");
//        //usuarioDTO.setPerfil("USER");
//
//        Usuario novoUsuario = Usuario.builder()
//                .id(1L)
//                .nome("João")
//                .email("joao@empresa.com")
//                .senha("123456")
//                //.perfil("USER")
//                .build();
//
//        // Mock: nenhum usuário existente
//        Mockito.when(usuarioService.buscarPorEmail(Mockito.eq("joao@empresa.com")))
//                .thenReturn(Optional.empty());
//        // Mock: salvar novo usuário
//        Mockito.when(usuarioService.salvar(Mockito.any(Usuario.class)))
//                .thenReturn(novoUsuario);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"nome\": \"João\", \"email\": \"joao@empresa.com\", \"senha\": \"123456\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.nome").value("João"))
//                .andExpect(jsonPath("$.email").value("joao@empresa.com"));
//                //.andExpect(jsonPath("$.perfil").value("USER"));
//
//        Mockito.verify(usuarioService).buscarPorEmail("joao@empresa.com");
//        Mockito.verify(usuarioService).salvar(Mockito.any(Usuario.class));
//    }


//    @Test
//    public void deveRetornarErroQuandoEmailJaCadastrado() throws Exception {
//        Usuario existente = new Usuario();
//        existente.setEmail("joao@empresa.com");
//
//        Mockito.when(usuarioService.buscarPorEmail(Mockito.eq("joao@empresa.com")))
//                .thenReturn(Optional.of(existente));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        //.content("{\"nome\": \"João\", \"email\": \"joao@empresa.com\", \"senha\": \"123456\", \"perfil\": \"USER\"}"))
//                        .content("{\"nome\": \"João\", \"email\": \"joao@empresa.com\", \"senha\": \"123456\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(org.hamcrest.Matchers.containsString("Email já cadastrado")));
//
//        Mockito.verify(usuarioService).buscarPorEmail("joao@empresa.com");
//        Mockito.verify(usuarioService, Mockito.never()).salvar(Mockito.any(Usuario.class));
//    }

}
