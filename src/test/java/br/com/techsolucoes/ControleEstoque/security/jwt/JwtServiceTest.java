package br.com.techsolucoes.ControleEstoque.security.jwt;

import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    private Usuario criarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");
        usuario.setPerfil(Perfil.OPERADOR);
        return usuario;
    }

    @Test
    void deveGerarTokenComUsernameValido() {
        Usuario usuario = criarUsuario("usuario@teste.com");

        String token = jwtService.generateToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairUsernameDeTokenValido() {
        Usuario usuario = criarUsuario("usuario@teste.com");

        String token = jwtService.generateToken(usuario);

        String usernameExtraido = jwtService.extractUsername(token);

        assertEquals(usuario.getEmail(), usernameExtraido);
    }

    @Test
    void deveValidarTokenCorretamente() {
        Usuario usuario = criarUsuario("usuario@teste.com");

        UserDetails userDetails = User
                .withUsername(usuario.getEmail())
                .password("123")
                .roles("USER")
                .build();

        String token = jwtService.generateToken(usuario);
        boolean valido = jwtService.isTokenValid(token, userDetails);
        assertTrue(valido);
    }

    @Test
    void naoDeveValidarTokenComUsernameIncorreto() {
        Usuario usuario = criarUsuario("usuario@teste.com");

        String token = jwtService.generateToken(usuario);

        UserDetails userDetails = User
                .withUsername("outro@teste.com")
                .password("123")
                .roles("USER")
                .build();

        boolean valido = jwtService.isTokenValid(token, userDetails);

        assertFalse(valido);
    }

    @Test
    void deveDetectarTokenExpirado() throws InterruptedException {
        JwtService servicoComExpiracaoCurta = new JwtService() {
            @Override
            public String generateToken(Usuario usuario) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(usuario.getEmail())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 100))
                        .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256,
                                "sua-chave-secreta-bem-grande-e-segura".getBytes())
                        .compact();
            }
        };

        Usuario usuario = criarUsuario("usuario@teste.com");

        String token = servicoComExpiracaoCurta.generateToken(usuario);

        Thread.sleep(200);

        assertThrows(ExpiredJwtException.class,
                () -> servicoComExpiracaoCurta.extractUsername(token));
    }

}
