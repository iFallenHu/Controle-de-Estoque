package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.mapper.FornecedorMapper;
import br.com.techsolucoes.ControleEstoque.service.FornecedorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FornecedorControllerTest {

    @Mock
    private FornecedorService fornecedorService;

    @Mock
    private FornecedorMapper fornecedorMapper;

    @InjectMocks
    private FornecedorController fornecedorController;

    @Test
    void DeveCadastrarFornecedor() {
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

    @Test
    void deveRetornarListaDeFornecedoresComSucesso() {
        // Arrange
        List<FornecedorResponseDTO> fornecedores = List.of(new FornecedorResponseDTO(), new FornecedorResponseDTO());
        when(fornecedorService.listar()).thenReturn(fornecedores);

        // Act
        ResponseEntity<List<FornecedorResponseDTO>> response = fornecedorController.listar();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(fornecedorService, times(1)).listar();
    }

    @Test
    void deveRetornarFornecedorPorIdComSucesso() {
        // Arrange
        Long id = 1L;
        Fornecedor fornecedor = new Fornecedor();
        when(fornecedorService.buscar(id)).thenReturn(fornecedor);

        // Act
        ResponseEntity<Fornecedor> response = fornecedorController.buscar(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fornecedor, response.getBody());
        verify(fornecedorService, times(1)).buscar(id);
    }

    @Test
    void deveRetornarNotFoundQuandoFornecedorNaoExiste() {
        // Arrange
        Long id = 1L;
        when(fornecedorService.buscar(id)).thenThrow(new CategoriaNotFoundException("Fornecedor não encontrado"));

        // Act
        ResponseEntity<Fornecedor> response = fornecedorController.buscar(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(fornecedorService, times(1)).buscar(id);
    }

    @Test
    void deveDeletarFornecedorComSucesso() {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<Void> response = fornecedorController.deletar(id);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(fornecedorService, times(1)).deletar(id);
    }

    @Test
    void deveRetornarNoContentQuandoFornecedorNaoEncontradoParaDelecao() {
        // Arrange
        Long id = 1L;
        doThrow(new CategoriaNotFoundException("Fornecedor não encontrado")).when(fornecedorService).deletar(id);

        // Act
        ResponseEntity<Void> response = fornecedorController.deletar(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(fornecedorService, times(1)).deletar(id);
    }

    @Test
    void deveAtualizarFornecedorComSucesso() {
        // Arrange
        Long id = 1L;
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        Fornecedor fornecedorAtualizado = new Fornecedor();

        when(fornecedorService.atualizar(id, dto)).thenReturn(fornecedorAtualizado);

        // Act
        ResponseEntity<Fornecedor> response = fornecedorController.atualizar(id, dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fornecedorAtualizado, response.getBody());
        verify(fornecedorService, times(1)).atualizar(id, dto);
    }


}
