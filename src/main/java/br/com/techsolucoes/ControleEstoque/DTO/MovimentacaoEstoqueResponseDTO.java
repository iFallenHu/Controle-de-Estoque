package br.com.techsolucoes.ControleEstoque.DTO;

import br.com.techsolucoes.ControleEstoque.entity.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoEstoqueResponseDTO(
        Long id,
        int quantidade,
        LocalDateTime data,
        TipoMovimentacao tipoMovimentacao,
        String motivo,
        ProdutoDTO produto,
        UsuarioDTO usuario
) {
    public record ProdutoDTO(Long id, String nome, String codigo) {}
    public record UsuarioDTO(Long id, String nome, String perfil) {}
}
