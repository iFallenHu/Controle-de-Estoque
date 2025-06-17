package br.com.techsolucoes.ControleEstoque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nome;

    @Column(length = 50, nullable = false)
    private String codigo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "unidadedeMedida", length = 20)
    private String unidadeMedida;

    @Column(name = "estoque_minimo")
    private Integer estoqueMinimo;

    @Column(name = "quantidade_atual")
    private Integer quantidadeAtual;

    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;


}
