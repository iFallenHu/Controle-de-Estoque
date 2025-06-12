package br.com.techsolucoes.ControleEstoque.mapper;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = "spring")
public interface FornecedorMapper {


    // Converte de DTO de entrada para entidade
    Fornecedor toEntity(FornecedorRequestDTO dto);

    // Converte de entidade para DTO de resposta
    FornecedorResponseDTO toDTO(Fornecedor fornecedor);

    // Lista de entidades para lista de DTOs
    List<FornecedorResponseDTO> toDTOList(List<Fornecedor> fornecedores);

    // Atualiza os dados de uma entidade existente com os dados do DTO
    void updateEntityFromDto(FornecedorRequestDTO dto, @MappingTarget Fornecedor fornecedor);
}


