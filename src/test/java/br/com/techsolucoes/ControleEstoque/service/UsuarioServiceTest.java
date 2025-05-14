package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;


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

        Mockito.when(usuarioRepository.findByEmail("joao@empresa.com"))
                .thenReturn(Optional.of(usuario));
        Mockito.when(passwordEncoder.matches("123456", "hashSenha"))
                .thenReturn(true);

        // Teste
        boolean autenticado = usuarioService.autenticar("joao@empresa.com", "123456");

        // Verifique se o mock foi chamado
        Mockito.verify(usuarioRepository).findByEmail(Mockito.anyString());

        // Verificação do resultado
        Assertions.assertTrue(autenticado);
    }


    @Test
    public void naoDeveAutenticarComSenhaErrada() {
        // Arrange (prepara)
        Usuario usuario = new Usuario();
        usuario.setEmail("joao@empresa.com");
        usuario.setSenha("hashSenha");  // senha criptografada salva no banco

        // Simula que o usuário existe no repositório
        Mockito.when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));

        // Simula que a senha informada não bate com a senha criptografada
        Mockito.when(passwordEncoder.matches("senhaErrada", "hashSenha"))
                .thenReturn(false);

        // Act (executa)
        boolean autenticado = usuarioService.autenticar("joao@empresa.com", "senhaErrada");

        // Assert (verifica)
        Assertions.assertFalse(autenticado);

        // Opcional: verifica se o repository foi mesmo chamado
        Mockito.verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    public void naoDeveAutenticarUsuarioInexistente() {
        // Arrange
        Mockito.when(usuarioRepository.findByEmail("inexistente@empresa.com"))
                .thenReturn(Optional.empty());

        // Act
        boolean autenticado = usuarioService.autenticar("inexistente@empresa.com", "qualquerSenha");

        // Assert
        Assertions.assertFalse(autenticado);

        // Verifica se o repository foi chamado corretamente
        Mockito.verify(usuarioRepository).findByEmail("inexistente@empresa.com");
    }

//    @Test
//    void eitaTest() {
//        Assertions.assertEquals(1,2);
//    }
}
