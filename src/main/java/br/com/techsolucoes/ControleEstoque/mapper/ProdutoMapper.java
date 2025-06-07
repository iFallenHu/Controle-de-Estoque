package br.com.techsolucoes.ControleEstoque.mapper;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    // Converte de DTO de entrada para entidade
    Produto toEntity(ProdutoRequestDTO dto);

    // Converte de entidade para DTO de resposta
    ProdutoResponseDTO toDTO(Produto produto);

    // Lista de entidades para lista de DTOs
    List<ProdutoResponseDTO> toDTOList(List<Produto> produtos);

    // Atualiza os dados de uma entidade existente com os dados do DTO
    void updateEntityFromDto(ProdutoRequestDTO dto, @MappingTarget Produto produto);
}
