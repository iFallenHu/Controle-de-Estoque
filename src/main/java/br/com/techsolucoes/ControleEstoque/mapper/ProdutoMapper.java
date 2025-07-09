package br.com.techsolucoes.ControleEstoque.mapper;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    // Converte de DTO de entrada para entidade
    Produto toEntity(ProdutoRequestDTO produtoRequestDTO);

    // Atualiza uma entidade existente com dados do DTO
    @Mapping(target = "id", ignore = true) // Ignora o ID ao fazer o merge
    @Mapping(target = "categoria", ignore = true) // Ignora porque será setado manualmente
    @Mapping(target = "fornecedor", ignore = true) // Também será setado manualmente
    void atualizarProdutoComDTO(ProdutoRequestDTO produtoRequestDTO, @MappingTarget Produto produto);

    // Converte de entidade para DTO de resposta
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "fornecedor.id", target = "fornecedorId")
    ProdutoResponseDTO toDTO(Produto produto);

    // Lista de entidades para lista de DTOs
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "fornecedor.id", target = "fornecedorId")
    List<ProdutoResponseDTO> toDTOList(List<Produto> produtos);


    // Atualiza os dados de uma entidade existente com os dados do DTO
    void updateEntityFromDto(ProdutoRequestDTO produtoRequestDTO, @MappingTarget Produto produto);
}
