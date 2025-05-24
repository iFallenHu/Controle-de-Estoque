package br.com.techsolucoes.ControleEstoque.repository;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
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

    @Test
    void deveListarTodosFornecedores() {
        Fornecedor c1 = new Fornecedor();
        c1.setId(1L);
        c1.setNome("Fornecedor1");

        Fornecedor c2 = new Fornecedor();
        c2.setId(2L);
        c2.setNome("Fornecedor2");

        List<Fornecedor> fornecedores = Arrays.asList(c1, c2);

        when(fornecedorRepository.findAll()).thenReturn(fornecedores);

        List<Fornecedor> resultado = fornecedorRepository.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Fornecedor1", resultado.get(0).getNome());
        assertEquals("Fornecedor2", resultado.get(1).getNome());
        verify(fornecedorRepository).findAll();
    }

    @Test
    void deveDeletarFornecedorPorId() {
        Long id = 1L;

        doNothing().when(fornecedorRepository).deleteById(id);

        fornecedorRepository.deleteById(id);

        verify(fornecedorRepository, times(1)).deleteById(id);
    }

    @Test
    void naoDeveSalvarFornecedorComErro() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Casas Bahia");
        fornecedor.setId(null);

        // Simulando uma exceção quando tentar salvar
        when(fornecedorRepository.save(any(Fornecedor.class)))
                .thenThrow(new RuntimeException("Erro ao salvar fornecedor"));

        try {
            fornecedorRepository.save(fornecedor);
        } catch (Exception e) {
            assertEquals("Erro ao salvar fornecedor", e.getMessage());
        }

        verify(fornecedorRepository).save(fornecedor);
    }

    // ------------------------- SCENARIO 2: Categoria não encontrada -------------------------
    @Test
    void naoDeveEncontrarFornecedorComIdInexistente() {
        // Simulando que não há fornecedor com esse id
        when(fornecedorRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Fornecedor> resultado = fornecedorRepository.findById(99L);

        assertFalse(resultado.isPresent());  // Espera-se que fornecedor não seja encontrada
        verify(fornecedorRepository).findById(99L);
    }

    // ------------------------- SCENARIO 3: Falha ao listar categorias -------------------------
    @Test
    void naoDeveListarFornecedorQuandoHouverErro() {
        // Simulando erro ao tentar recuperar a lista
        when(fornecedorRepository.findAll()).thenThrow(new RuntimeException("Erro ao listar fornecedores"));

        try {
            fornecedorRepository.findAll();
        } catch (Exception e) {
            assertEquals("Erro ao listar fornecedores", e.getMessage());
        }

        verify(fornecedorRepository).findAll();
    }

    // ------------------------- SCENARIO 4: Falha ao deletar fornecedor -------------------------
    @Test
    void naoDeveDeletarFornecedorQuandoNaoExistir() {
        Long idInexistente = 99L;

        // Simulando erro ao tentar deletar
        doThrow(new RuntimeException("Erro ao deletar fornecedor")).when(fornecedorRepository).deleteById(idInexistente);

        try {
            fornecedorRepository.deleteById(idInexistente);
        } catch (Exception e) {
            assertEquals("Erro ao deletar fornecedor", e.getMessage());
        }

        verify(fornecedorRepository).deleteById(idInexistente);
    }
}
