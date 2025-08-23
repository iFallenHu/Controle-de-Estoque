package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.MovimentacaoEstoqueRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.MovimentacaoEstoqueResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.MovimentacaoEstoque;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.entity.TipoMovimentacao;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.exception.EstoqueInsuficienteException;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.repository.MovimentacaoEstoqueRepository;
import br.com.techsolucoes.ControleEstoque.repository.ProdutoRepository;
import br.com.techsolucoes.ControleEstoque.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {
    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public MovimentacaoEstoqueResponseDTO registrarMovimentacao(MovimentacaoEstoqueRequestDTO dto) {
        Produto produto= produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Atualiza a quantidade do produto
        if (dto.getTipoMovimentacao() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() + dto.getQuantidade());
        } else if (dto.getTipoMovimentacao() == TipoMovimentacao.SAIDA) {
            if (produto.getQuantidadeAtual() < dto.getQuantidade()) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para saída. Quantidade disponível: "
                        + produto.getQuantidadeAtual() + ", solicitada: " + dto.getQuantidade());
            }
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() - dto.getQuantidade());
        }

        produtoRepository.save(produto);

        // Cria o registro da movimentação
        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .quantidade(dto.getQuantidade())
                .data(LocalDateTime.now())
                .tipoMovimentacao(dto.getTipoMovimentacao())
                .motivo(dto.getMotivo())
                .produto(produto)
                .usuario(usuario)
                .build();

        movimentacaoRepository.save(movimentacao);

        return new MovimentacaoEstoqueResponseDTO(
                movimentacao.getId(),
                movimentacao.getQuantidade(),
                movimentacao.getData(),
                movimentacao.getTipoMovimentacao(),
                movimentacao.getMotivo(),
                new MovimentacaoEstoqueResponseDTO.ProdutoDTO(
                      produto.getId(),
                      produto.getNome(),
                      produto.getCodigo()
                ),
                new MovimentacaoEstoqueResponseDTO.UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getPerfil().name()
                )
        );
    }
}
