package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.CategoriaDTO;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.entity.Categoria;
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

//    @PostMapping
//    public ResponseEntity<Categoria> criarCategoria(@RequestBody Categoria categoria) {
//        try {
//            Categoria novaCategoria = categoriaService.salvarCategoria(categoria);
//            return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//
//        }
//    }
    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try {
            Categoria novaCategoria = categoriaService.salvarCategoria(categoriaDTO);
            return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarCategoria() {
        try {
            return ResponseEntity.ok(categoriaService.listarCategoria());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
