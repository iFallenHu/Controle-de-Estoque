package br.com.techsolucoes.ControleEstoque.DTO;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProdutoRequestDTO {


    private String nome;
    private String codigo;
    private String descricao;
    private String unidadeMedida;
    private Integer estoqueMinimo;
    private Integer quantidadeAtual;
    private BigDecimal preco;
    private Long categoriaId;
    private Long fornecedorId;
}
