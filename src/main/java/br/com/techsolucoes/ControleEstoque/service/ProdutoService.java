package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Categoria;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.mapper.ProdutoMapper;
import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import br.com.techsolucoes.ControleEstoque.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
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

        Produto produto = produtoMapper.toEntity(produtoRequestDTO);
        produto.setCategoria(categoria);
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);
    }

}
