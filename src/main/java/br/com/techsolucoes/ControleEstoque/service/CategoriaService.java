package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.model.Categoria;
import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository; //referencia da instancia da interface.

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria salvarCategoria(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategoria(){
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(long id){
        return categoriaRepository.findById( id)
        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public void deletarCategoria(long id){
       Categoria categoria = categoriaRepository.findById(id)
               .orElseThrow(()->new RuntimeException("Categoria não encontrada"));

               categoriaRepository.deleteById(id);
        }
}
