package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.model.Categoria;
import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveRetornarCategoriaQuandoIdExistir() {

        //Arrange
        Long id = 1L;

        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(id);
        categoriaMock.setNome("Eletrônicos");

        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.of(categoriaMock));

        // Act
        Categoria resultado = categoriaService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals("Eletrônicos", resultado.getNome());
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        Long id = 999L;
        Mockito.when(categoriaRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            categoriaService.buscarPorId(id);
        });
    }

    @Test
    void deveSalvarCategoriaComSucesso(){

        //Arrange
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome("Informática");

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome("Informática");

        Mockito.when(categoriaRepository.save(novaCategoria))
                .thenReturn(categoriaSalva);


        //Act

        Categoria resultado = categoriaService.salvarCategoria(novaCategoria);

        //Assert

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Informática", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaForNull(){
        assertThrows(IllegalArgumentException.class, () -> {
           categoriaService.salvarCategoria(null);
        });

    }

    @Test
    void deveListarCategoriasComSucesso(){
        //Arrange
        Categoria cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNome("Informática");

        Categoria cat2 =new Categoria();
        cat2.setId(2L);
        cat2.setNome("Alimentos");

        List<Categoria> categorias = Arrays.asList(cat1, cat2);

        Mockito.when(categoriaRepository.findAll()).thenReturn(categorias);

        //Act
        List<Categoria> resultado = categoriaService.listarCategoria();

        //Assert
        assertNotNull(resultado);
        assertEquals(2,resultado.size());
        assertEquals("Informática", resultado.get(0).getNome());
        assertEquals("Alimentos", resultado.get(1).getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistiremCategorias(){
        //Arrange
        Mockito.when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

        //Act & Assert
        assertThrows(IllegalStateException.class, () -> categoriaService.listarCategoria());
    }
}
