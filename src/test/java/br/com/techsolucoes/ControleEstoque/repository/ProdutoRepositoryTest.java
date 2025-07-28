package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.mapper.ProdutoMapper;
import br.com.techsolucoes.ControleEstoque.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoRepositoryTest {

    @Mock
    ProdutoRepository produtoRepository;
    @InjectMocks
    ProdutoService produtoService;

    @Test
    void deveCriarUmProduto(){
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Teclado Gamer");
        produto.setCodigo("TG123");
        produto.setDescricao("Teclado mecânico RGB");
        produto.setPreco(BigDecimal.valueOf(299.90));
        // preencha os outros campos conforme necessário

        // simula o comportamento do repository
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto produtoSalvo = produtoRepository.save(produto);

        // verifica se o retorno está correto
        assertNotNull(produtoSalvo);
        assertEquals("Teclado Gamer", produtoSalvo.getNome());
        assertEquals("TG123", produtoSalvo.getCodigo());

        // verifica se o método save foi chamado uma vez
        verify(produtoRepository, times(1)).save(produto);

    }

    @Test
    void deveLancarExcecaoAoSalvarProdutoInvalido() {
        Produto produtoInvalido = new Produto(); // campos obrigatórios não preenchidos

        when(produtoRepository.save(produtoInvalido))
                .thenThrow(new IllegalArgumentException("Dados inválidos para salvar o produto"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoRepository.save(produtoInvalido);
        });

        assertEquals("Dados inválidos para salvar o produto", exception.getMessage());

        verify(produtoRepository, times(1)).save(produtoInvalido);
    }

    @Test
    void deveBuscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Mouse");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Optional<Produto> resultado = produtoRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Mouse", resultado.get().getNome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarVazioAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Produto> resultado = produtoRepository.findById(99L);

        assertFalse(resultado.isPresent());
        verify(produtoRepository, times(1)).findById(99L);
    }

    @Test
    void deveListarTodosOsProdutos() {
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Teclado");
        produto1.setCodigo("COD123");

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Mouse");
        produto2.setCodigo("COD456");

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> resultado = produtoRepository.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        assertEquals("Mouse", resultado.get(1).getNome());

        verify(produtoRepository, times(1)).findAll();
    }


    @Test
    void deveDeletarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);

        doNothing().when(produtoRepository).delete(produto);

        produtoRepository.delete(produto);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoNulo() {
        doThrow(new IllegalArgumentException("Produto não pode ser nulo"))
                .when(produtoRepository).delete(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produtoRepository.delete(null);
        });

        assertEquals("Produto não pode ser nulo", exception.getMessage());
        verify(produtoRepository, times(1)).delete(null);
    }


}
