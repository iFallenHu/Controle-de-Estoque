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

    @Test
    void naoDeveSalvarCategoriaComErro() {
        Categoria categoria = new Categoria();
        categoria.setNome("Tecnologia");
        categoria.setId(null);

        // Simulando uma exceção quando tentar salvar
        when(categoriaRepository.save(any(Categoria.class)))
                .thenThrow(new RuntimeException("Erro ao salvar categoria"));

        try {
            categoriaRepository.save(categoria);
        } catch (Exception e) {
            assertEquals("Erro ao salvar categoria", e.getMessage());
        }

        verify(categoriaRepository).save(categoria);
    }

    // ------------------------- SCENARIO 2: Categoria não encontrada -------------------------
    @Test
    void naoDeveEncontrarCategoriaPorId() {
        // Simulando que não há categoria com esse id
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Categoria> resultado = categoriaRepository.findById(99L);

        assertFalse(resultado.isPresent());  // Espera-se que a categoria não seja encontrada
        verify(categoriaRepository).findById(99L);
    }

    // ------------------------- SCENARIO 3: Falha ao listar categorias -------------------------
    @Test
    void naoDeveListarCategoriasQuandoHouverErro() {
        // Simulando erro ao tentar recuperar a lista
        when(categoriaRepository.findAll()).thenThrow(new RuntimeException("Erro ao listar categorias"));

        try {
            categoriaRepository.findAll();
        } catch (Exception e) {
            assertEquals("Erro ao listar categorias", e.getMessage());
        }

        verify(categoriaRepository).findAll();
    }

    // ------------------------- SCENARIO 4: Falha ao deletar categoria -------------------------
    @Test
    void naoDeveDeletarCategoriaQuandoNaoExistir() {
        Long idInexistente = 99L;

        // Simulando erro ao tentar deletar
        doThrow(new RuntimeException("Erro ao deletar categoria")).when(categoriaRepository).deleteById(idInexistente);

        try {
            categoriaRepository.deleteById(idInexistente);
        } catch (Exception e) {
            assertEquals("Erro ao deletar categoria", e.getMessage());
        }

        verify(categoriaRepository).deleteById(idInexistente);
    }
}

