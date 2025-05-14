package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public void cadastrar(FornecedorRequestDTO fornecedorRequestDTO) {
        if (fornecedorRepository.existsByCnpj(fornecedorRequestDTO.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado.");
        }

        if (fornecedorRepository.existsByEmail(fornecedorRequestDTO.getEmail())) {
            throw new RuntimeException("E-mail á cadastrado.");
        }

        Fornecedor fornecedor = Fornecedor.builder()
                .nome(fornecedorRequestDTO.getNome())
                .cnpj(fornecedorRequestDTO.getCnpj())
                .telefone(fornecedorRequestDTO.getTelefone())
                .email(fornecedorRequestDTO.getEmail())
                .build();

        fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> listarFornecedor() {
        List<Fornecedor> fornecedor = fornecedorRepository.findAll();
        if (fornecedor.isEmpty()) {
            throw new IllegalStateException("Nenhuma categoria encontrada.");
        }
        return fornecedor;
    }

    public Fornecedor buscarPorId(long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria com ID" + id + "Não encontrada"));
    }

    public void deletarFornecedor(long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fornecedorRepository.deleteById(id);
    }

    public Fornecedor atualizarFornecedor(long id, FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        fornecedor.setNome(fornecedorRequestDTO.getNome());
        return fornecedorRepository.save(fornecedor);
    }
}
