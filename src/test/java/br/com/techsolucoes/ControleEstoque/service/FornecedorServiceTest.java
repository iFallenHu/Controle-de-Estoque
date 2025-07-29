package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Mock
    private FornecedorMapper fornecedorMapper;


    @Test
    void DeveCadastrarFornecedor() {
        // Arrange
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Fornecedor X");
        dto.setCnpj("12345678000190");
        dto.setTelefone("11999999999");
        dto.setEmail("email@teste.com");

        when(fornecedorRepository.existsByCnpj(dto.getCnpj())).thenReturn(false);
        when(fornecedorRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        // Act
        assertDoesNotThrow(() -> fornecedorService.cadastrar(dto));

        // Assert
        verify(fornecedorRepository).save(fornecedorMapper.toEntity(dto));
    }

    @Test
    void NaoDeveCadastrarFornecedorComCnpjJaCadastrado() {
        // Arrange
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Fornecedor X");
        dto.setCnpj("12345678000190");
        dto.setTelefone("11999999999");
        dto.setEmail("email@teste.com");

        when(fornecedorRepository.existsByCnpj(dto.getCnpj())).thenReturn(true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> fornecedorService.cadastrar(dto));
        assertEquals("CNPJ já cadastrado.", ex.getMessage());

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void NaoDeveCadastrarFornecedorComEmailJaCadastrado() {
        // Arrange
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Fornecedor X");
        dto.setCnpj("12345678000190");
        dto.setTelefone("11999999999");
        dto.setEmail("email@teste.com");

        when(fornecedorRepository.existsByCnpj(dto.getCnpj())).thenReturn(false);
        when(fornecedorRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> fornecedorService.cadastrar(dto));
        assertEquals("E-mail já cadastrado.", ex.getMessage());

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void DeveListarFornecedor() {
        // Arrange: crie os dados falsos
        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setId(1L);
        fornecedor1.setNome("Fornecedor A");

        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setId(2L);
        fornecedor2.setNome("Fornecedor B");

        List<Fornecedor> fornecedores = List.of(fornecedor1, fornecedor2);

        FornecedorResponseDTO dto1 = new FornecedorResponseDTO();
        dto1.setId(1L);
        dto1.setNome("Fornecedor A");

        FornecedorResponseDTO dto2 = new FornecedorResponseDTO();
        dto2.setId(2L);
        dto2.setNome("Fornecedor B");

        List<FornecedorResponseDTO> dtos = List.of(dto1, dto2);

        // Simula o comportamento dos mocks
        when(fornecedorRepository.findAll()).thenReturn(fornecedores);
        when(fornecedorMapper.toDTOList(fornecedores)).thenReturn(dtos);

        // Act: chama o método real
        List<FornecedorResponseDTO> resultado = fornecedorService.listar();

        // Assert: verifica se veio o esperado
        assertEquals(2, resultado.size());
        assertEquals("Fornecedor A", resultado.get(0).getNome());
        assertEquals("Fornecedor B", resultado.get(1).getNome());
    }

    @Test
    void DeveInformarErroListarSeNaoEncontrarFornecedor() {
        // Arrange
        when(fornecedorRepository.findAll()).thenReturn(List.of()); // Simula lista vazia

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> fornecedorService.listar()
        );

        assertEquals("Nenhum fornecedor encontrado.", exception.getMessage());
    }

    @Test
    void DeveDeletarFornecedor() {
        // Arrange
        long id = 1L;

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(id);

        when(fornecedorRepository.findById(id)).thenReturn(java.util.Optional.of(fornecedor));

        // Act
        assertDoesNotThrow(() -> fornecedorService.deletar(id));

        // Assert
        verify(fornecedorRepository).deleteById(id);
    }

    @Test
    void NaoDeveDeletarFornecedorQuandoNaoEncontrado() {
        long id = 999L;

        when(fornecedorRepository.findById(id)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                fornecedorService.deletar(id)
        );

        assertEquals("Fornecedor com ID 999 não encontrado.", exception.getMessage());
        verify(fornecedorRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveAtualizarFornecedor() {
        // Arrange
        Long id = 1L;

        Fornecedor fornecedorExistente = new Fornecedor();
        fornecedorExistente.setId(id);
        fornecedorExistente.setNome("Antigo Nome");
        fornecedorExistente.setCnpj("11111111000199");
        fornecedorExistente.setEmail("antigo@email.com");

        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Novo Nome");
        dto.setCnpj("12345678000199");
        dto.setEmail("novo@email.com");

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(fornecedorExistente));
        when(fornecedorRepository.existsByCnpjAndIdNot(dto.getCnpj(), id)).thenReturn(false);
        when(fornecedorRepository.existsByEmailAndIdNot(dto.getEmail(), id)).thenReturn(false);

        // Simula que o mapper atualiza o objeto existente
        doAnswer(invocation -> {
            FornecedorRequestDTO source = invocation.getArgument(0);
            Fornecedor target = invocation.getArgument(1);
            target.setNome(source.getNome());
            target.setCnpj(source.getCnpj());
            target.setEmail(source.getEmail());
            return null;
        }).when(fornecedorMapper).updateEntityFromDto(eq(dto), eq(fornecedorExistente));

        when(fornecedorRepository.save(fornecedorExistente)).thenReturn(fornecedorExistente);

        // Act
        Fornecedor atualizado = fornecedorService.atualizar(id, dto);

        // Assert
        assertNotNull(atualizado);
        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals("12345678000199", atualizado.getCnpj());
        assertEquals("novo@email.com", atualizado.getEmail());

        verify(fornecedorRepository).findById(id);
        verify(fornecedorRepository).save(fornecedorExistente);
        verify(fornecedorMapper).updateEntityFromDto(dto, fornecedorExistente);
    }


    @Test
    void NaoDeveAtualizarFornecedorQuandoNaoEncontrado() {
        long id = 999L;
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Nome Qualquer");

        when(fornecedorRepository.findById(id)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                fornecedorService.atualizar(id, dto)
        );

        assertEquals("Fornecedor não encontrado", exception.getMessage());
    }


}