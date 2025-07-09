package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.Categoria;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByNomeAndCategoriaAndFornecedor(String nome, Categoria categoria, Fornecedor fornecedor);
}
