package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.model.Categoria;
import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
