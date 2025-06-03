    package br.com.techsolucoes.ControleEstoque.repository;

    import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
    import org.springframework.data.jpa.repository.JpaRepository;

    public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

        boolean existsByCnpj(String cnpj);
        boolean existsByEmail(String email);

        boolean existsByCnpjAndIdNot(String cnpj, Long id);
        boolean existsByEmailAndIdNot(String email, Long id);

    }
