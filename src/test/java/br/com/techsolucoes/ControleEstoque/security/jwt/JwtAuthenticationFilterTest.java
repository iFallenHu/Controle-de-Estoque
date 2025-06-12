package br.com.techsolucoes.ControleEstoque.security.jwt;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Test
    void deveIgnorarFiltroParaRotaPublica() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveIgnorarQuandoAuthorizationHeaderAusente() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/alguma-rota");
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void deveAutenticarUsuarioComTokenValido() throws Exception {
        String token = "valid.token.jwt";
        String username = "usuario@teste.com";

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/teste");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(userDetails.getUsername()).thenReturn(username);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void naoDeveAutenticarUsuarioComTokenInvalido() throws Exception {
        String token = "invalid.token.jwt";
        String username = "usuario@teste.com";

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/teste");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // Limpa o contexto para não interferir nos outros testes
    }
}
