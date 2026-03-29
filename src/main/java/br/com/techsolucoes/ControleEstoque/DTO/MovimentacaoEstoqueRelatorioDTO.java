package br.com.techsolucoes.ControleEstoque.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimentacaoEstoqueRelatorioDTO {

    private Long id;
    private LocalDateTime data;
    private Integer quantidade;
    private String tipoMovimentacao;
    private String motivo;
    private String produtoNome;
    private String usuarioNome;
}
