package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.repository.FornecedorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private FornecedorService fornecedorService;

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
        verify(fornecedorRepository).save(any(Fornecedor.class));
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
        assertEquals("E-mail á cadastrado.", ex.getMessage());

        verify(fornecedorRepository, never()).save(any());
    }

        @Test
        void DeveListarFornecedor(){

        }


}