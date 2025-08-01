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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoMapper produtoMapper;

    private final CategoriaRepository categoriaRepository;

    private final FornecedorRepository fornecedorRepository;

    public Produto cadastrar(ProdutoRequestDTO produtoRequestDTO) {

        Categoria categoria = categoriaRepository.findById(produtoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Fornecedor fornecedor = fornecedorRepository.findById(produtoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Optional<Produto> produtoExistente = produtoRepository.findByNomeAndCategoriaAndFornecedor(
                produtoRequestDTO.getNome(), categoria, fornecedor);

        if (produtoExistente.isPresent()) {
            throw new DuplicateResourceException("Produto já cadastrado com esta categoria e fornecedor");
        }

        Produto produto = produtoMapper.toEntity(produtoRequestDTO);
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);
    }

    public List<ProdutoResponseDTO> listar() {
        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum fornecedor encontrado.");
        }

        return produtoMapper.toDTOList(produtos);
    }

    public Produto buscar(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " Não encontrada"));
    }
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado."));

        produtoRepository.deleteById(produto.getId());
    }


    public Produto atualizar(Long id, ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        // Atualiza os campos do produto com base no DTO
        produtoMapper.atualizarProdutoComDTO(produtoRequestDTO, produtoExistente);

        // Atualiza as entidades relacionadas, se necessário
        Categoria categoria = categoriaRepository.findById(produtoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + produtoRequestDTO.getCategoriaId()));
        Fornecedor fornecedor = fornecedorRepository.findById(produtoRequestDTO.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado com ID: " + produtoRequestDTO.getFornecedorId()));

        produtoExistente.setCategoria(categoria);
        produtoExistente.setFornecedor(fornecedor);

        return produtoRepository.save(produtoExistente);
    }
}
