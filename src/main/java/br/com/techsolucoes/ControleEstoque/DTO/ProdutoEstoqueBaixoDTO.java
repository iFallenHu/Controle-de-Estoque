package br.com.techsolucoes.ControleEstoque.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoEstoqueBaixoDTO {
    private Long id;
    private String nome;
    private Integer quantidadeAtual;
    private Integer quantidadeEstoqueMinimo;
}
