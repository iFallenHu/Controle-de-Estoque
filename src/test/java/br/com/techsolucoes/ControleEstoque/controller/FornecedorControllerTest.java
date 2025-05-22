package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import br.com.techsolucoes.ControleEstoque.service.FornecedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FornecedorControllerTest {

    @Mock
    private FornecedorService fornecedorService;

    @Mock
    private FornecedorMapper fornecedorMapper;

    @InjectMocks
    private FornecedorController fornecedorController;

    @Test
    void DeveTestarMetodoPost() {
        // Arrange
        FornecedorRequestDTO fornecedorRequestDTO = new FornecedorRequestDTO();
        // preencha os campos se necessário, ex:
        // fornecedorRequestDTO.setNome("Fornecedor Teste");

        // Act
        ResponseEntity<Void> response = fornecedorController.cadastrar(fornecedorRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(fornecedorService, times(1)).cadastrar(fornecedorRequestDTO);
    }
}
