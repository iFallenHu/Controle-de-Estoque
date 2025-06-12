package br.com.techsolucoes.ControleEstoque.security.jwt;

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

    @Test
    void deveGerarTokenComUsernameValido() {
        String username = "usuario@teste.com";

        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveExtrairUsernameDeTokenValido() {
        String username = "usuario@teste.com";
        String token = jwtService.generateToken(username);

        String usernameExtraido = jwtService.extractUsername(token);

        assertEquals(username, usernameExtraido);
    }

    @Test
    void deveValidarTokenCorretamente() {
        String username = "usuario@teste.com";
        UserDetails userDetails = User.withUsername(username).password("123").roles("USER").build();
        String token = jwtService.generateToken(username);

        boolean valido = jwtService.isTokenValid(token, userDetails);

        assertTrue(valido);
    }

    @Test
    void naoDeveValidarTokenComUsernameIncorreto() {
        String token = jwtService.generateToken("usuario@teste.com");
        UserDetails userDetails = User.withUsername("outro@teste.com").password("123").roles("USER").build();

        boolean valido = jwtService.isTokenValid(token, userDetails);

        assertFalse(valido);
    }

    @Test
    void deveDetectarTokenExpirado() throws InterruptedException {
        JwtService servicoComExpiracaoCurta = new JwtService() {
            @Override
            public String generateToken(String username) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 100)) // 100ms
                        .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, "sua-chave-secreta-bem-grande-e-segura".getBytes())
                        .compact();
            }
        };

        String token = servicoComExpiracaoCurta.generateToken("usuario@teste.com");

        Thread.sleep(200); // espera expirar

        assertThrows(ExpiredJwtException.class, () -> servicoComExpiracaoCurta.extractUsername(token));
    }

}
