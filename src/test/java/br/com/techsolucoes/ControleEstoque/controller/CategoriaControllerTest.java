package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.model.Categoria;
import br.com.techsolucoes.ControleEstoque.service.CategoriaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import static org.junit.jupiter.api.Assertions.*;
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

        //Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Tecnologia");

        when(categoriaService.salvarCategoria(any())).thenReturn(categoria);

        //Act
        ResponseEntity<Categoria> response = categoriaController.criarCategoria(categoria);

        //Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tecnologia", response.getBody().getNome());
    }

    @Test
    void deveRetornarError500QuandoServiceLancarExcecao() {

        //Arrange
        Categoria categoria = new Categoria();
        categoria.setNome("Tecnologia");

        when(categoriaService.salvarCategoria(any()))
                .thenThrow(new RuntimeException("Erro ao salvar categoria"));

        //Act
        ResponseEntity<Categoria> response = categoriaController.criarCategoria(categoria);

        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    void deveBuscarIdERetornar200(){

        //Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Tecnologia");


        when(categoriaService.buscarPorId(any(Long.class))).thenReturn(categoria);

        //Act

        ResponseEntity<Categoria> response = categoriaController.buscarPorId(1L);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tecnologia", response.getBody().getNome());

    }

    @Test
    void deveRetornar404QuandoCategoriaNaoEncontrada(){
        Long idInexistente = 99L;

        when(categoriaService.buscarPorId(idInexistente))
                .thenThrow(new CategoriaNotFoundException("Categoria não encontrada"));

        ResponseEntity<Categoria> response = categoriaController.buscarPorId(idInexistente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}