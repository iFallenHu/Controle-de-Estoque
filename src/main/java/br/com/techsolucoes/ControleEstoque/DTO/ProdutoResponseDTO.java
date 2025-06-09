package br.com.techsolucoes.ControleEstoque.DTO;

import java.math.BigDecimal;

public class ProdutoResponseDTO {

    private Long id;
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
