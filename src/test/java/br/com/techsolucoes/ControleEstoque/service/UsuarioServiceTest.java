package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.UsuarioRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.exception.EmailJaCadastradoException;
import br.com.techsolucoes.ControleEstoque.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ModelMapper modelMapper;


    @Test
    public void deveAutenticarComSenhaCorreta() {
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@empresa.com");
        usuario.setSenha("hashSenha");

        // Configuração do mock
//        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString()))
//                .thenReturn(Optional.of(usuario));
//
//        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(true);

        when(usuarioRepository.findByEmail("joao@empresa.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "hashSenha"))
                .thenReturn(true);

        // Teste
        boolean autenticado = usuarioService.autenticar("joao@empresa.com", "123456");

        // Verifique se o mock foi chamado
        Mockito.verify(usuarioRepository).findByEmail(Mockito.anyString());

        // Verificação do resultado
        assertTrue(autenticado);
    }


    @Test
    public void naoDeveAutenticarComSenhaErrada() {
        // Arrange (prepara)
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@empresa.com");
        usuario.setSenha("hashSenha");  // senha criptografada salva no banco

        // Simula que o usuário existe no repositório
        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        // Simula que a senha informada não bate com a senha criptografada
        when(passwordEncoder.matches("senhaErrada", "hashSenha"))
                .thenReturn(false);

        // Act (executa)
        boolean autenticado = usuarioService.autenticar("joao@empresa.com", "senhaErrada");

        // Assert (verifica)
        assertFalse(autenticado);

        // Opcional: verifica se o repository foi mesmo chamado
        Mockito.verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    public void naoDeveAutenticarUsuarioInexistente() {
        // Arrange
        when(usuarioRepository.findByEmail("inexistente@empresa.com"))
                .thenReturn(Optional.empty());

        // Act
        boolean autenticado = usuarioService.autenticar("inexistente@empresa.com", "qualquerSenha");

        // Assert
        assertFalse(autenticado);

        // Verifica se o repository foi chamado corretamente
        Mockito.verify(usuarioRepository).findByEmail("inexistente@empresa.com");
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        UsuarioRequestDTO request = new UsuarioRequestDTO("João", "joao@email.com", "123", Perfil.ADMIN);
        Usuario usuarioEntity = new Usuario(1L, "João", "joao@email.com", "senhaCodificada", Perfil.ADMIN);
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.ADMIN);

        when(passwordEncoder.encode("123")).thenReturn("senhaCodificada");
        when(modelMapper.map(request, Usuario.class)).thenReturn(usuarioEntity);
        when(usuarioRepository.save(usuarioEntity)).thenReturn(usuarioEntity);
        when(modelMapper.map(usuarioEntity, UsuarioResponseDTO.class)).thenReturn(response);

        UsuarioResponseDTO result = usuarioService.salvar(request);

        assertEquals(response, result);
    }

    @Test
    void deveRetornarTrueQuandoCredenciaisForemValidas() {
        Usuario usuario = new Usuario(1L, "Ana", "ana@email.com", "senhaHash", Perfil.OPERADOR);
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123", "senhaHash")).thenReturn(true);

        boolean autenticado = usuarioService.autenticar("ana@email.com", "123");

        assertTrue(autenticado);
    }

    @Test
    void deveRetornarFalseQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        boolean autenticado = usuarioService.autenticar("naoexiste@email.com", "123456");

        assertFalse(autenticado);
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverCadastrado() {
        when(usuarioRepository.findByEmail("email@email.com")).thenReturn(Optional.of(new Usuario()));

        assertThrows(EmailJaCadastradoException.class, () -> usuarioService.verificarEmailDuplicado("email@email.com"));
    }

    @Test
    void deveListarUsuarios() {
        Usuario u1 = new Usuario(1L, "João", "joao@email.com", "senha", Perfil.OPERADOR);
        Usuario u2 = new Usuario(2L, "Maria", "maria@email.com", "senha", Perfil.ADMIN);
        List<Usuario> usuarios = List.of(u1, u2);

        UsuarioResponseDTO dto1 = new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.OPERADOR);
        UsuarioResponseDTO dto2 = new UsuarioResponseDTO(2L, "Maria", "maria@email.com", Perfil.ADMIN);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(modelMapper.map(u1, UsuarioResponseDTO.class)).thenReturn(dto1);
        when(modelMapper.map(u2, UsuarioResponseDTO.class)).thenReturn(dto2);

        List<UsuarioResponseDTO> result = usuarioService.listar();

        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getNome());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Usuario usuario = new Usuario(1L, "João", "joao@email.com", "senha", Perfil.OPERADOR);
        UsuarioResponseDTO dto = new UsuarioResponseDTO(1L, "João", "joao@email.com", Perfil.OPERADOR);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(dto);

        UsuarioResponseDTO result = usuarioService.buscarPorId(1L);

        assertEquals("João", result.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPorIdInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void deveDeletarUsuarioExistente() {
        Usuario usuario = new Usuario(1L, "João", "joao@email.com", "senha", Perfil.OPERADOR);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(1L);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario usuario = new Usuario(1L, "João", "joao@email.com", "senha", Perfil.OPERADOR);
        UsuarioRequestDTO dto = new UsuarioRequestDTO("João Atualizado", "joao@email.com", "123", Perfil.ADMIN);
        Usuario atualizado = new Usuario(1L, "João Atualizado", "joao@email.com", "senha", Perfil.ADMIN);
        UsuarioResponseDTO response = new UsuarioResponseDTO(1L, "João Atualizado", "joao@email.com", Perfil.ADMIN);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(atualizado);
        when(modelMapper.map(atualizado, UsuarioResponseDTO.class)).thenReturn(response);

        UsuarioResponseDTO result = usuarioService.atualizar(1L, dto);

        assertEquals("João Atualizado", result.getNome());
    }

    @Test
    void autenticar2DeveRetornarTrueQuandoAutenticacaoForBemSucedida() {
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.isAuthenticated()).thenReturn(true);

        boolean resultado = usuarioService.autenticar2("email", "senha");

        assertTrue(resultado);
    }

    @Test
    void autenticar2DeveRetornarFalseQuandoAutenticacaoFalhar() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("erro"));

        boolean resultado = usuarioService.autenticar2("email", "senha");

        assertFalse(resultado);
    }

}
