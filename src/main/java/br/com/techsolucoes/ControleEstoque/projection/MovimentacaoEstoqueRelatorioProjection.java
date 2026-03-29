package br.com.techsolucoes.ControleEstoque.projection;

import java.time.LocalDateTime;

public interface MovimentacaoEstoqueRelatorioProjection {

    Long getId();
    LocalDateTime getData();
    Integer getQuantidade();
    String getTipoMovimentacao();
    String getMotivo();
    String getProdutoNome();
    String getUsuarioNome();
}
