package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.ResourceNotFoundException;
import br.com.techsolucoes.ControleEstoque.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @Operation(summary = "cadastro de fornecedor", description = "Efetua o cadastro de uma Empresa.")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid FornecedorRequestDTO fornecedorRequestDTO) {
        fornecedorService.cadastrar(fornecedorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Listar fornecedores", description = "Retorna a lista de todos os fornecedores cadastrados.")
    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listar() {
        try {
            List<FornecedorResponseDTO> fornecedores = fornecedorService.listar();
            return ResponseEntity.ok(fornecedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna os dados de um fornecedor com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscar(@PathVariable Long id) {
        try {
            Fornecedor fornecedor = fornecedorService.buscar(id);
            return ResponseEntity.ok(fornecedor);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar fornecedor", description = "Remove um fornecedor do sistema com base no ID informado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Atualizar fornecedor", description = "Atualiza os dados de um fornecedor com base no ID informado.")
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @RequestBody FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedorAtualizado = fornecedorService.atualizar(id, fornecedorRequestDTO);
        return ResponseEntity.ok(fornecedorAtualizado);
    }

}
