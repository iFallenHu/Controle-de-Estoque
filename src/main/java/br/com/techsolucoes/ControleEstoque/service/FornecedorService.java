package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.DuplicateResourceException;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    private final FornecedorMapper fornecedorMapper;

    public void cadastrar(FornecedorRequestDTO fornecedorRequestDTO) {
        if (fornecedorRepository.existsByCnpj(fornecedorRequestDTO.getCnpj())) {
            throw new DuplicateResourceException("CNPJ já cadastrado.");
        }

        if (fornecedorRepository.existsByEmail(fornecedorRequestDTO.getEmail())) {
            throw new DuplicateResourceException("E-mail já cadastrado.");
        }

        fornecedorRepository.save(fornecedorMapper.toEntity(fornecedorRequestDTO));
    }

    public List<FornecedorResponseDTO> listar() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();

        if (fornecedores.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum fornecedor encontrado.");
        }

        return fornecedorMapper.toDTOList(fornecedores);
    }

    public Fornecedor buscar(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + id + " Não encontrada"));
    }

    public void deletar(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado."));

        fornecedorRepository.deleteById(fornecedor.getId());
    }

    public Fornecedor atualizar(Long id, FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));


        if (fornecedorRepository.existsByCnpjAndIdNot(fornecedorRequestDTO.getCnpj(), id)) {
            throw new DuplicateResourceException("CNPJ já cadastrado.");
        }

        if (fornecedorRepository.existsByEmailAndIdNot(fornecedorRequestDTO.getEmail(), id)) {
            throw new DuplicateResourceException("E-mail já cadastrado.");
        }

        // Atualiza os dados usando o mapper
        fornecedorMapper.updateEntityFromDto(fornecedorRequestDTO, fornecedor);


        return fornecedorRepository.save(fornecedor);
    }
}
