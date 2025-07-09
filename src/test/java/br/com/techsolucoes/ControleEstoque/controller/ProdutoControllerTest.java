package br.com.techsolucoes.ControleEstoque.controller;


import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.exception.DuplicateResourceException;
import br.com.techsolucoes.ControleEstoque.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @Mock
    ProdutoService produtoService;

  @InjectMocks
    ProdutoController produtoController;

    @Test
    void deveCriarProdutoERetornar201(){
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
    void deveRetornarErroQuandoProdutoJaExiste(){
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

        assertThatThrownBy(()-> produtoController.cadastrar(produtoRequestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Produto já cadastrado");

        verify(produtoService).cadastrar(produtoRequestDTO);
    }

}
