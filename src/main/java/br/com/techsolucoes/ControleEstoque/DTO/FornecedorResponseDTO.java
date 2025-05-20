    package br.com.techsolucoes.ControleEstoque.DTO;

    import lombok.Data;

    @Data
    public class FornecedorResponseDTO {

        private Long id;
        private String nome;
        private String cnpj;
        private String telefone;
        private String email;
    }
