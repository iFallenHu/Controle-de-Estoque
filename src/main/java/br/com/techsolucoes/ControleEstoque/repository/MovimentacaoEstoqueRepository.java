package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.entity.MovimentacaoEstoque;
import br.com.techsolucoes.ControleEstoque.projection.MovimentacaoEstoqueRelatorioProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query(value = """
            SELECT 
                me.id as id,
                me.data as data,
                me.quantidade as quantidade,
                me.tipo_movimentacao as tipoMovimentacao,
                me.motivo as motivo,
                p.nome as produtoNome,
                u.nome as usuarioNome
            FROM movimentacao_estoque me
            JOIN produto p ON p.id = me.produto_id
            JOIN usuario u ON u.id = me.usuario_id
            WHERE me.data BETWEEN :dataInicio AND :dataFim
            ORDER BY me.data DESC
            """, nativeQuery = true)
    List<MovimentacaoEstoqueRelatorioProjection> buscarRelatorio(
            LocalDateTime dataInicio,
            LocalDateTime dataFim);
}
