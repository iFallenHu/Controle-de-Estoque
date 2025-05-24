package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FornecedorRepositoryTest {

    @Mock
    private FornecedorRepository fornecedorRepository;
    @Mock
    private FornecedorMapper fornecedorMapper;

    @Test
    void DeveCriarFornecedor() {
        // Arrange
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Fornecedor Exemplo");
        dto.setCnpj("98765432000100");

        Fornecedor entidade = new Fornecedor();
        entidade.setNome("Fornecedor Exemplo");
        entidade.setCnpj("98765432000100");

        when(fornecedorMapper.toEntity(dto)).thenReturn(entidade);

        //Act
        Fornecedor convertido = fornecedorMapper.toEntity(dto);

        //Assert

        assertNotNull(convertido);
        assertEquals(dto.getNome(), convertido.getNome());
        assertEquals(dto.getCnpj(), convertido.getCnpj());

    }

    @Test
    void DeveAtualizarFornecedor() {
        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(1L);
        fornecedorExistente.setNome("Fornecedor exemplo");
        fornecedorExistente.setCnpj("98765432000100");

        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setId(1L);
        fornecedorAtualizado.setNome("Fornecedor exemplo1");
        fornecedorAtualizado.setCnpj("98765432000101");

        when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorAtualizado);

        Optional<Fornecedor> encontrado = fornecedorRepository.findById(1L);
        assertTrue(encontrado.isPresent());

        Fornecedor atualizado = encontrado.get();
        atualizado.setNome("Fornecedor exemplo1");

        Fornecedor resultado = fornecedorRepository.save(atualizado);

        assertEquals("Fornecedor exemplo1", resultado.getNome());
        verify(fornecedorRepository).save(atualizado);
    }
}
