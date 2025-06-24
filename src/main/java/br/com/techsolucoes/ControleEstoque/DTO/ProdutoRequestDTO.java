package br.com.techsolucoes.ControleEstoque.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProdutoRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "O código não pode estar em branco")
    private String codigo;

    private String descricao;

    @NotBlank(message = "A unidade de medida é obrigatória")
    private String unidadeMedida;

    @NotNull(message = "O estoque mínimo é obrigatório")
    @Min(value = 0, message = "O estoque mínimo não pode ser negativo")
    private Integer estoqueMinimo;

    @NotNull(message = "A quantidade atual é obrigatória")
    @Min(value = 0, message = "A quantidade atual não pode ser negativa")
    private Integer quantidadeAtual;

    @NotNull(message = "O preço do produto é obrigatório")

    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "A informação de qual categoria pertence é obrigatória")
    private Long categoriaId;

    @NotNull(message = "A informação de qual fornecedor pertence é obrigatória")
    private Long fornecedorId;
}
