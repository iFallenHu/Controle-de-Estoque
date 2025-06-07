package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.ProdutoRequestDTO;
import br.com.techsolucoes.ControleEstoque.entity.Produto;
import br.com.techsolucoes.ControleEstoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
