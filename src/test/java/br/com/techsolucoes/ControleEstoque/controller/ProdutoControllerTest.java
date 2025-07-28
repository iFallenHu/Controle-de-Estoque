package br.com.techsolucoes.ControleEstoque.controller;


import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.exception.DuplicateResourceException;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @Mock
    ProdutoService produtoService;

    @InjectMocks
    ProdutoController produtoController;

    @Test
    void deveCriarProdutoERetornar201() {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setNome("Produto Teste");
        produtoRequestDTO.setCodigo("COD123");
        produtoRequestDTO.setDescricao("Descricao teste");
        produtoRequestDTO.setUnidadeMedida("UN");
        produtoRequestDTO.setEstoqueMinimo(5);
        produtoRequestDTO.setQuantidadeAtual(20);
        produtoRequestDTO.setPreco(BigDecimal.valueOf(99.90));
        produtoRequestDTO.setCategoriaId(1L);
        produtoRequestDTO.setFornecedorId(1L);

        produtoController.cadastrar(produtoRequestDTO);

        verify(produtoService, times(1)).cadastrar(produtoRequestDTO);
    }

    @Test
    void deveRetornarErroQuandoProdutoJaExiste() {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setNome("Produto Teste");
        produtoRequestDTO.setCodigo("COD123");
        produtoRequestDTO.setDescricao("Descricao teste");
        produtoRequestDTO.setUnidadeMedida("UN");
        produtoRequestDTO.setEstoqueMinimo(5);
        produtoRequestDTO.setQuantidadeAtual(20);
        produtoRequestDTO.setPreco(BigDecimal.valueOf(99.90));
        produtoRequestDTO.setCategoriaId(1L);
        produtoRequestDTO.setFornecedorId(1L);

        doThrow(new DuplicateResourceException("Produto já cadastrado"))
                .when(produtoService)
                .cadastrar(produtoRequestDTO);

        assertThatThrownBy(() -> produtoController.cadastrar(produtoRequestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Produto já cadastrado");

        verify(produtoService).cadastrar(produtoRequestDTO);
    }

    @Test
    void deveListarProdutoERetornar201() {
        ProdutoResponseDTO produto1 = new ProdutoResponseDTO();
        produto1.setNome("Produto Teste");
        produto1.setCodigo("COD123");
        produto1.setDescricao("Descricao teste");
        produto1.setUnidadeMedida("UN");
        produto1.setEstoqueMinimo(5);
        produto1.setQuantidadeAtual(20);
        produto1.setPreco(BigDecimal.valueOf(99.90));
        produto1.setCategoriaId(1L);
        produto1.setFornecedorId(1L);


        ProdutoResponseDTO produto2 = new ProdutoResponseDTO();
        produto2.setNome("Produto Teste2");
        produto2.setCodigo("COD1234");
        produto2.setDescricao("Descricao teste2");
        produto2.setUnidadeMedida("ml");
        produto2.setEstoqueMinimo(6);
        produto2.setQuantidadeAtual(21);
        produto2.setPreco(BigDecimal.valueOf(99.95));
        produto2.setCategoriaId(2L);
        produto2.setFornecedorId(2L);

        List<ProdutoResponseDTO> produtos = Arrays.asList(produto1, produto2);
        when(produtoService.listar()).thenReturn(produtos);

        ResponseEntity<List<ProdutoResponseDTO>> response = produtoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Produto Teste", response.getBody().get(0).getNome());

    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremProdutos() {
        // Simula que o service retorna uma lista vazia
        when(produtoService.listar()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProdutoResponseDTO>> response = produtoController.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void deveBuscarUmProdutoPeloId() {
        // Arrange
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome("Produto Teste");
        produto.setCodigo("COD123");
        produto.setDescricao("Descrição teste");
        produto.setUnidadeMedida("UN");
        produto.setEstoqueMinimo(5);
        produto.setQuantidadeAtual(10);
        produto.setPreco(BigDecimal.valueOf(99.90));
        // Adicione categoria e fornecedor se forem objetos dentro de Produto

        when(produtoService.buscar(id)).thenReturn(produto);

        // Act
        ResponseEntity<Produto> response = produtoController.buscar(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Produto Teste", response.getBody().getNome());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void deveRetornar404QuandoProdutoNaoEncontrado() {
        Long id = 99L;
        when(produtoService.buscar(id)).thenThrow(new ResourceNotFoundException(("Produto nao encontrado")));

        assertThrows(ResourceNotFoundException.class, () -> {
            produtoController.buscar(id);
        });
    }

    @Test
    void deveDeletarUmProdutoPeloId() {
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);

        doNothing().when(produtoService).deletar(id);

        produtoController.deletar(id);

        verify(produtoService).deletar(id);
    }

    @Test
    void deveRetornar404() {
        doThrow(new ResourceNotFoundException("Produto não encontrado"))
                .when(produtoService).deletar(999L);

        assertThrows(ResourceNotFoundException.class, () -> {
            produtoController.deletar(999L);
        });

    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        // Arrange
        Long id = 1L;

        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Produto Atualizado");
        dto.setCodigo("PROD999");
        dto.setDescricao("Nova descrição");
        dto.setUnidadeMedida("UN");
        dto.setEstoqueMinimo(10);
        dto.setQuantidadeAtual(50);
        dto.setPreco(BigDecimal.valueOf(199.99));
        dto.setCategoriaId(2L);
        dto.setFornecedorId(3L);

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(id);
        produtoAtualizado.setNome(dto.getNome());
        produtoAtualizado.setCodigo(dto.getCodigo());
        produtoAtualizado.setDescricao(dto.getDescricao());
        produtoAtualizado.setUnidadeMedida(dto.getUnidadeMedida());
        produtoAtualizado.setEstoqueMinimo(dto.getEstoqueMinimo());
        produtoAtualizado.setQuantidadeAtual(dto.getQuantidadeAtual());
        produtoAtualizado.setPreco(dto.getPreco());

        when(produtoService.atualizar(id, dto)).thenReturn(produtoAtualizado);

        ResponseEntity<Produto> response = produtoController.atualizar(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Produto Atualizado", response.getBody().getNome());
        assertEquals("PROD999", response.getBody().getCodigo());
        assertEquals(BigDecimal.valueOf(199.99), response.getBody().getPreco());
    }

    @Test
    void deveRetornarErroQuandoProdutoNaoExisteParaAtualizar() {
        Long id = 999L;
        ProdutoRequestDTO dto = new ProdutoRequestDTO();

        doThrow(new ResourceNotFoundException("Produto não encontrado"))
                .when(produtoService).atualizar(id, dto);

        assertThrows(ResourceNotFoundException.class, () -> {
            produtoController.atualizar(id, dto);
        });
    }



}
