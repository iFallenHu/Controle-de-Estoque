package br.com.techsolucoes.ControleEstoque.service;

import br.com.techsolucoes.ControleEstoque.DTO.CategoriaDTO;
import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
import br.com.techsolucoes.ControleEstoque.entity.Categoria;
import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository; //referencia da instancia da interface.

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria salvarCategoria(Categoria categoria) {
        if (categoria == null){
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategoria() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            throw new IllegalStateException("Nenhuma categoria encontrada.");
        }
        return categorias;
    }

    public Categoria buscarPorId(long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoria com ID" + id + "Não encontrada"));
    }

    public void deletarCategoria(long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoriaRepository.deleteById(id);
    }

    public Categoria atualizarCategoria(long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoria.setNome(dto.getNome());
        return categoriaRepository.save(categoria);
    }


}
