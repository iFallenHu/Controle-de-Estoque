package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.CategoriaDTO;
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

    public CategoriaController(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.salvarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    @GetMapping
    public List<Categoria> listarCategoria(){
        return categoriaService.listarCategoria();
    }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable long id){
        return categoriaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable long id){
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build(); //Retorna 204
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable long id, @RequestBody CategoriaDTO dto){
        Categoria categoriaAtualizada = categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);
    }


}
