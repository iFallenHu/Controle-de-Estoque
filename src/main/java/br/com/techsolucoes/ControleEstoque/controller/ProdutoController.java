package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.ProdutoResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.mapper.ProdutoMapper;
import br.com.techsolucoes.ControleEstoque.service.ProdutoService;
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
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(summary = "cadastro de Produto", description = "Efetua o cadastro de um produto.")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO){
        produtoService.cadastrar(produtoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Listar Produtos", description = "Retorna a lista de todos os produtos cadastrados.")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar(){
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoService.listar();
        return ResponseEntity.ok(produtoResponseDTOS);
    }

    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna os dados de um produto com base no ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long id) {
        Produto produto = produtoService.buscar(id);
        return ResponseEntity.ok(produto);
    }

    @Operation(summary = "Deletar produto", description = "Remove um produto do sistema com base no ID informado.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();

    }

    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto com base no ID informado.")
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoAtualizado = produtoService.atualizar(id, produtoRequestDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }
}
