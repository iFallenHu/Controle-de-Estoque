package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.Categoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoriaRepositoryTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Test
    void deveSalvarCategoriaComMockito() {
        Categoria categoria = new Categoria();
        categoria.setNome("Tecnologia");
        categoria.setId(null);

        Categoria categoriaSalva = new Categoria();
        categoriaSalva.setId(1L);
        categoriaSalva.setNome("Tecnologia");

        when(categoriaRepository.save(any(Categoria.class)))
                .thenReturn(categoriaSalva);

        Categoria resultado = categoriaRepository.save(categoria);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tecnologia", resultado.getNome());

        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    void deveAtualizarCategoria() {
        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setId(1L);
        categoriaExistente.setNome("Tecnologia");

        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(1L);
        categoriaAtualizada.setNome("Eletrônicos");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoriaAtualizada);

        Optional<Categoria> encontrada = categoriaRepository.findById(1L);
        assertTrue(encontrada.isPresent());

        Categoria atualizada = encontrada.get();
        atualizada.setNome("Eletrônicos");

        Categoria resultado = categoriaRepository.save(atualizada);

        assertEquals("Eletrônicos", resultado.getNome());
        verify(categoriaRepository).save(atualizada);
    }

    @Test
    void deveListarTodasCategorias() {
        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setNome("Livros");

        Categoria c2 = new Categoria();
        c2.setId(2L);
        c2.setNome("Esporte");

        List<Categoria> categorias = Arrays.asList(c1, c2);

        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaRepository.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Livros", resultado.get(0).getNome());
        assertEquals("Esporte", resultado.get(1).getNome());
        verify(categoriaRepository).findAll();
    }

    @Test
    void deveDeletarCategoriaPorId() {
        Long id = 1L;

        doNothing().when(categoriaRepository).deleteById(id);

        categoriaRepository.deleteById(id);

        verify(categoriaRepository, times(1)).deleteById(id);
    }

}