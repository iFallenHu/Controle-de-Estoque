package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.model.Categoria;
import br.com.techsolucoes.ControleEstoque.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @Test
    void deveCriarCategoriaERetornar201() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Tecnologia");

        when(categoriaService.salvarCategoria(any())).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.criarCategoria(categoria);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tecnologia", response.getBody().getNome());
    }
}