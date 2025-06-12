package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
//@EntityScan(basePackages = "br.com.techsolucoes.ControleEstoque.entity")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveSalvarEEncontrarUsuarioPorEmail() {
        // Arrange: criar usuário
        Usuario usuario = Usuario.builder()
                .nome("João")
                .email("joao@empresa.com")
                .senha("123456")
                .perfil(Perfil.OPERADOR)
                .build();

        // Act: salvar no banco
        usuarioRepository.save(usuario);

        // Act: buscar pelo e-mail
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("joao@empresa.com");

        // Assert: verificar se foi encontrado
        Assertions.assertTrue(encontrado.isPresent());
        Assertions.assertEquals("João", encontrado.get().getNome());
    }

    @Test
    public void deveRetornarVazioQuandoEmailNaoExistir() {
        // Act: buscar um e-mail que não existe
        Optional<Usuario> resultado = usuarioRepository.findByEmail("inexistente@empresa.com");

        // Assert
        Assertions.assertFalse(resultado.isPresent());
    }

    @Test
    public void deveSalvarEMostrarTodosUsuarios() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .nome("Maria")
                .email("maria@empresa.com")
                .senha("abc123")
                .perfil(Perfil.ADMIN)
                .build();

        Usuario usuario2 = Usuario.builder()
                .nome("Carlos")
                .email("carlos@empresa.com")
                .senha("xyz789")
                .perfil(Perfil.OPERADOR)
                .build();

        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        // Act
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Assert
        Assertions.assertEquals(2, usuarios.size());
        Assertions.assertTrue(usuarios.stream().anyMatch(u -> u.getEmail().equals("maria@empresa.com")));
        Assertions.assertTrue(usuarios.stream().anyMatch(u -> u.getEmail().equals("carlos@empresa.com")));
    }

    @Test
    public void deveLancarExcecaoQuandoEmailDuplicado() {
        // Arrange
        Usuario usuario1 = Usuario.builder()
                .nome("Lucas")
                .email("lucas@empresa.com")
                .senha("senha1")
                .perfil(Perfil.OPERADOR)
                .build();

        Usuario usuario2 = Usuario.builder()
                .nome("Lucas Clone")
                .email("lucas@empresa.com") // mesmo e-mail
                .senha("senha2")
                .perfil(Perfil.OPERADOR)
                .build();

        usuarioRepository.save(usuario1);

        // Act + Assert
        Assertions.assertThrows(Exception.class, () -> {
            usuarioRepository.saveAndFlush(usuario2);
        });
    }
}
