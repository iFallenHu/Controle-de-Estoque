package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    private final FornecedorMapper fornecedorMapper;

    public FornecedorService(FornecedorRepository fornecedorRepository, FornecedorMapper fornecedorMapper) {
        this.fornecedorRepository = fornecedorRepository;
        this.fornecedorMapper = fornecedorMapper;
    }

    public void cadastrar(FornecedorRequestDTO fornecedorRequestDTO) {
        if (fornecedorRepository.existsByCnpj(fornecedorRequestDTO.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado.");
        }

        if (fornecedorRepository.existsByEmail(fornecedorRequestDTO.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        fornecedorRepository.save(fornecedorMapper.toEntity(fornecedorRequestDTO));
    }

    public List<FornecedorResponseDTO> listar() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();

        if (fornecedores.isEmpty()) {
            throw new IllegalStateException("Nenhum fornecedor encontrado.");
        }

        return fornecedorMapper.toDTOList(fornecedores);
    }

    public Fornecedor buscar(long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Fornecedor com ID" + id + "Não encontrada"));
    }

    public void deletar(long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fornecedorRepository.deleteById(fornecedor.getId());
    }

    public Fornecedor atualizar(long id, FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrada"));

        fornecedor.setNome(fornecedorRequestDTO.getNome());
        return fornecedorRepository.save(fornecedor);
    }
}
