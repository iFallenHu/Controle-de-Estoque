package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.FornecedorRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.FornecedorResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Fornecedor;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody @Validated FornecedorRequestDTO fornecedorRequestDTO) {
        fornecedorService.cadastrar(fornecedorRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listar() {
        try {
            List<FornecedorResponseDTO> fornecedores = fornecedorService.listar();
            return ResponseEntity.ok(fornecedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscar(@PathVariable long id) {
        try {
            Fornecedor fornecedor = fornecedorService.buscar(id);
            return ResponseEntity.ok(fornecedor);
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        try {
            fornecedorService.deletar(id);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.noContent().build(); //Retorna 204
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable long id, @RequestBody FornecedorRequestDTO fornecedorRequestDTO) {
        Fornecedor fornecedorAtualizado = fornecedorService.atualizar(id, fornecedorRequestDTO);
        return ResponseEntity.ok(fornecedorAtualizado);
    }

}
