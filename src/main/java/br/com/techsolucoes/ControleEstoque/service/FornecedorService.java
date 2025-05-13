    package br.com.techsolucoes.ControleEstoque.service;

    import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
    import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
    import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    @RequiredArgsConstructor
    @Service
    public class FornecedorService {

        private final FornecedorRepository fornecedorRepository;

        public void cadastrar(FornecedorRequestDTO fornecedorRequestDTO){
           if (fornecedorRepository.existsByCnpj(fornecedorRequestDTO.getCnpj())) {
                throw new RuntimeException("CNPJ já cadastrado.");
            }

           if (fornecedorRepository.existsByEmail(fornecedorRequestDTO.getEmail())){
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
    }
