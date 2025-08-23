package br.com.techsolucoes.ControleEstoque.DTO;

import br.com.techsolucoes.ControleEstoque.entity.TipoMovimentacao;
import lombok.Data;

@Data
public class MovimentacaoEstoqueRequestDTO {
    private Long produtoId;
    private Long usuarioId;
    private Integer quantidade;
    private TipoMovimentacao tipoMovimentacao;
    private String motivo;
}
