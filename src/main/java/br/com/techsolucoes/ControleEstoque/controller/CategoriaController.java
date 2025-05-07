package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.CategoriaDTO;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.model.Categoria;
import br.com.techsolucoes.ControleEstoque.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria novaCategoria = categoriaService.salvarCategoria(categoria);
            return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategoria() {
        List<Categoria> categoria = categoriaService.listarCategoria();
        if (categoria.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable long id) {
        try {
            Categoria categoria = categoriaService.buscarPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (CategoriaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable long id) {
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build(); //Retorna 204
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable long id, @RequestBody CategoriaDTO dto) {
        Categoria categoriaAtualizada = categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);
    }



}
