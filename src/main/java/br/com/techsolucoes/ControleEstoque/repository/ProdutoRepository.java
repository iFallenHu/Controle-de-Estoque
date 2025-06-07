package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
