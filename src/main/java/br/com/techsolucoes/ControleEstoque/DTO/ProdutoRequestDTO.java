package br.com.techsolucoes.ControleEstoque.DTO;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProdutoRequestDTO {


    private String nome;
    private String codigo;
    private String descricao;
    private String unidadeMedida;
    private Integer estoqueMinimo = 0;
    private Integer quantidadeAtual = 0;
    private BigDecimal preco;
    private Long categoriaId;
    private Long fornecedorId;
}
