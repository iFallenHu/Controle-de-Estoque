package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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
}
