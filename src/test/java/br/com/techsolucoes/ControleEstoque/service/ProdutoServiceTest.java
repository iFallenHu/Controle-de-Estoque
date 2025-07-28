package br.com.techsolucoes.ControleEstoque.service;


import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Categoria;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.exception.DuplicateResourceException;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.mapper.ProdutoMapper;
import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import br.com.techsolucoes.ControleEstoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void deveCadastrarProdutoSemErro() {
        // Arrange - DTO de entrada
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Produto Teste");
        dto.setCodigo("123");
        dto.setDescricao("Teste");
        dto.setUnidadeMedida("UN");
        dto.setEstoqueMinimo(1);
        dto.setQuantidadeAtual(10);
        dto.setPreco(BigDecimal.valueOf(10.0));
        dto.setCategoriaId(1L);
        dto.setFornecedorId(1L);

        // Mockando os dados retornados pelos repositórios
        Categoria categoria = new Categoria();
        Fornecedor fornecedor = new Fornecedor();
        Produto produtoEntity = new Produto();

        // Mocks
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.findByNomeAndCategoriaAndFornecedor("Produto Teste", categoria, fornecedor))
                .thenReturn(Optional.empty());
        when(produtoMapper.toEntity(dto)).thenReturn(produtoEntity);
        when(produtoRepository.save(produtoEntity)).thenReturn(produtoEntity);

        // Act
        Produto resultado = produtoService.cadastrar(dto);

        // Assert (opcional)
        assertNotNull(resultado); // Verifica se não retornou null
        verify(produtoRepository).save(produtoEntity); // Verifica se o save foi chamado
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaNaoEncontrada() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setCategoriaId(1L);
        dto.setFornecedorId(1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.cadastrar(dto));
    }

    @Test
    void deveLancarExcecaoQuandoFornecedorNaoEncontrado() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setCategoriaId(1L);
        dto.setFornecedorId(1L);

        Categoria categoria = new Categoria();
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> produtoService.cadastrar(dto));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoJaExistente() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Produto Teste");
        dto.setCategoriaId(1L);
        dto.setFornecedorId(1L);

        Categoria categoria = new Categoria();
        Fornecedor fornecedor = new Fornecedor();
        Produto produtoExistente = new Produto();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.findByNomeAndCategoriaAndFornecedor("Produto Teste", categoria, fornecedor))
                .thenReturn(Optional.of(produtoExistente));

        assertThrows(DuplicateResourceException.class, () -> produtoService.cadastrar(dto));
    }

    @Test
    void deveListarProdutosComSucesso() {
        // Simula produtos do banco
        Produto produto1 = new Produto();
        Produto produto2 = new Produto();
        List<Produto> produtos = List.of(produto1, produto2);

        // Simula resposta mapeada
        ProdutoResponseDTO dto1 = new ProdutoResponseDTO();
        ProdutoResponseDTO dto2 = new ProdutoResponseDTO();
        List<ProdutoResponseDTO> dtos = List.of(dto1, dto2);

        // Mocks
        when(produtoRepository.findAll()).thenReturn(produtos);
        when(produtoMapper.toDTOList(produtos)).thenReturn(dtos);

        // Chamada
        List<ProdutoResponseDTO> resultado = produtoService.listar();

        // Verificação
        assertEquals(2, resultado.size());
        verify(produtoRepository).findAll();
        verify(produtoMapper).toDTOList(produtos);
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistiremProdutos() {
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.listar());

        verify(produtoRepository).findAll();
        verifyNoInteractions(produtoMapper); // Mapper não deve ser chamado
    }

    @Test
    void deveBuscarProdutoPorId() {
        Produto produto = new Produto();
        produto.setId(1L);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscar(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontradoPorId() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.buscar(1L));
    }

    @Test
    void deveAtualizarProduto() {
        Long produtoId = 1L;

        // Dados do DTO com as atualizações
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Atualizado");
        dto.setCodigo("A456");
        dto.setDescricao("Descrição atualizada");
        dto.setUnidadeMedida("UN");
        dto.setEstoqueMinimo(5);
        dto.setQuantidadeAtual(20);
        dto.setPreco(new BigDecimal("15.50"));
        dto.setCategoriaId(1L);
        dto.setFornecedorId(1L);

        // Produto existente no banco (antes da atualização)
        Produto produtoExistente = new Produto();
        produtoExistente.setId(produtoId);
        produtoExistente.setNome("Antigo");

        // Mock de categoria e fornecedor
        Categoria categoria = new Categoria();
        Fornecedor fornecedor = new Fornecedor();

        // Mocka os repositórios
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));
        when(categoriaRepository.findById(dto.getCategoriaId())).thenReturn(Optional.of(categoria));
        when(fornecedorRepository.findById(dto.getFornecedorId())).thenReturn(Optional.of(fornecedor));
        when(produtoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Simula o comportamento do mapper void (atualizando manualmente)
        doAnswer(invocation -> {
            ProdutoRequestDTO dtoArg = invocation.getArgument(0);
            Produto produtoArg = invocation.getArgument(1);
            produtoArg.setNome(dtoArg.getNome());
            produtoArg.setCodigo(dtoArg.getCodigo());
            produtoArg.setDescricao(dtoArg.getDescricao());
            produtoArg.setUnidadeMedida(dtoArg.getUnidadeMedida());
            produtoArg.setEstoqueMinimo(dtoArg.getEstoqueMinimo());
            produtoArg.setQuantidadeAtual(dtoArg.getQuantidadeAtual());
            produtoArg.setPreco(dtoArg.getPreco());
            return null;
        }).when(produtoMapper).atualizarProdutoComDTO(eq(dto), eq(produtoExistente));

        // Executa o método a ser testado
        Produto resultado = produtoService.atualizar(produtoId, dto);

        // Verifica se os dados foram atualizados
        assertEquals("Atualizado", resultado.getNome());
        assertEquals("A456", resultado.getCodigo());
        assertEquals("Descrição atualizada", resultado.getDescricao());
        assertEquals("UN", resultado.getUnidadeMedida());
        assertEquals(5, resultado.getEstoqueMinimo());
        assertEquals(20, resultado.getQuantidadeAtual());
        assertEquals(new BigDecimal("15.50"), resultado.getPreco());
        assertEquals(categoria, resultado.getCategoria());
        assertEquals(fornecedor, resultado.getFornecedor());

        verify(produtoMapper).atualizarProdutoComDTO(dto, produtoExistente);
    }

    @Test
    void deveLancarExcecao_QuandoProdutoNaoEncontrado() {
        Long produtoId = 999L;
        ProdutoRequestDTO dto = new ProdutoRequestDTO();

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            produtoService.atualizar(produtoId, dto);
        });

        assertEquals("Produto não encontrado com ID: 999", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoCategoriaNaoEncontrada() {
        Long produtoId = 1L;
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setCategoriaId(10L); // Categoria inexistente
        dto.setFornecedorId(1L);

        Produto produtoExistente = new Produto();

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produtoExistente));
        when(categoriaRepository.findById(dto.getCategoriaId()))
                .thenReturn(Optional.empty());

        // Precisa mockar produtoMapper.atualizarProdutoComDTO
        doNothing().when(produtoMapper).atualizarProdutoComDTO(any(), any());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            produtoService.atualizar(produtoId, dto);
        });

        assertEquals("Categoria não encontrada com ID: 10", exception.getMessage());
    }

    @Test
    void deveLancarExcecao_QuandoFornecedorNaoEncontrado() {
        Long produtoId = 1L;
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setCategoriaId(1L);
        dto.setFornecedorId(99L); // Fornecedor inexistente

        Produto produtoExistente = new Produto();
        Categoria categoria = new Categoria();

        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(produtoExistente));
        when(categoriaRepository.findById(dto.getCategoriaId()))
                .thenReturn(Optional.of(categoria));
        when(fornecedorRepository.findById(dto.getFornecedorId()))
                .thenReturn(Optional.empty());

        doNothing().when(produtoMapper).atualizarProdutoComDTO(any(), any());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            produtoService.atualizar(produtoId, dto);
        });

        assertEquals("Fornecedor não encontrado com ID: 99", exception.getMessage());
    }

    @Test
    void deveDeletarProduto() {
        Long id = 1L;
        Produto produto = new Produto();
        produto.setId(id);

        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        produtoService.deletar(id);

        verify(produtoRepository).findById(id);
        verify(produtoRepository).deleteById(id);
    }


    @Test
    void deveLancarExcecaoAoTentarDeletarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> produtoService.deletar(1L));
    }


}
