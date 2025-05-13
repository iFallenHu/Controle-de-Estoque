package br.com.techsolucoes.ControleEstoque.controller;

    import br.com.techsolucoes.ControleEstoque.DTO.CategoriaDTO;
    import br.com.techsolucoes.ControleEstoque.exception.CategoriaNotFoundException;
    import br.com.techsolucoes.ControleEstoque.entity.Categoria;
    import br.com.techsolucoes.ControleEstoque.service.CategoriaService;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.junit.jupiter.MockitoExtension;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;

    import java.util.Arrays;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.ArgumentMatchers.any;
    import static org.mockito.ArgumentMatchers.eq;
    import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class CategoriaControllerTest {

        @Mock
        private CategoriaService categoriaService;

        @InjectMocks
        private CategoriaController categoriaController;

        @Test
        void deveCriarCategoriaERetornar201() {

            Categoria categoria = new Categoria();
            categoria.setNome("Tecnologia");
        //Arrange
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Tecnologia");

        when(categoriaService.salvarCategoria(any())).thenReturn(categoria);

        //Act
        ResponseEntity<Categoria> response = categoriaController.criarCategoria(categoriaDTO);

        //Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Tecnologia", response.getBody().getNome());
    }

    @Test
    void deveRetornarError500QuandoServiceLancarExcecao() {

        //Arrange

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Tecnologia");

        when(categoriaService.salvarCategoria(any()))
                .thenThrow(new RuntimeException("Erro ao salvar categoria"));

        //Act
        ResponseEntity<Categoria> response = categoriaController.criarCategoria(categoriaDTO);

        //Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    void deveBuscarIdERetornar200() {

        //Arrange
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Tecnologia");


        when(categoriaService.buscarPorId(any(Long.class))).thenReturn(categoria);

        //Act

        ResponseEntity<Categoria> response = categoriaController.buscarPorId(1L);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Tecnologia", response.getBody().getNome());

    }

    @Test
    void deveRetornar404QuandoCategoriaNaoEncontrada() {
        Long idInexistente = 99L;

        when(categoriaService.buscarPorId(idInexistente))
                .thenThrow(new CategoriaNotFoundException("Categoria não encontrada"));

        ResponseEntity<Categoria> response = categoriaController.buscarPorId(idInexistente);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deveRetornarListaCategoriaERetornar200() {
        //Arrange

        Categoria cat1 = new Categoria();
        cat1.setId(1L);
        cat1.setNome("Informática");

        Categoria cat2 = new Categoria();
        cat2.setId(2L);
        cat2.setNome("Alimentos");

        List<Categoria> categoria = Arrays.asList(cat1, cat2);
        when(categoriaService.listarCategoria()).thenReturn(categoria);

        //Act
        ResponseEntity<List<Categoria>> response = categoriaController.listarCategoria();

        //Assert

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Informática", response.getBody().get(0).getNome());


    }

    @Test
    void deveRetornar500QuandoServicoLancarExcecao() {
        // Arrange
        when(categoriaService.listarCategoria()).thenThrow(new RuntimeException("Erro simulado"));

        // Act
        ResponseEntity<List<Categoria>> response = categoriaController.listarCategoria();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deveAtualizarUmaEntidadeExistenteERetornar200() {
        // Arrange
        Long id = 1L;

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Eletrônicos");

        Categoria categoriaAtualizada = new Categoria();
        categoriaAtualizada.setId(id);
        categoriaAtualizada.setNome("Eletrônicos");

        when(categoriaService.atualizarCategoria(id, categoriaDTO)).thenReturn(categoriaAtualizada);

        // Act
        ResponseEntity<Categoria> response = categoriaController.atualizarCategoria(id, categoriaDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Eletrônicos", response.getBody().getNome());

    }

    @Test
    void deveRetornarErro500QuandoAtualizacaoCategoriaFalhar() {
        // Arrange
        Long id = 1L;
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNome("Nova Categoria");

        // Simula exceção no service
        when(categoriaService.atualizarCategoria(eq(id), any(CategoriaDTO.class)))
                .thenThrow(new RuntimeException("Erro ao atualizar categoria"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            categoriaController.atualizarCategoria(id, dto);
        });
    }

    @Test
    void deveDeletarUmaEntidadeERetornar200() {
        Long id = 1L;

        // Simula que não acontece erro ao deletar
        doNothing().when(categoriaService).deletarCategoria(eq(id));

        // Executa o controller
        ResponseEntity<Void> response = categoriaController.deletarCategoria(id);

        // Verifica se o status é 204 No Content
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void deveRetornarErro500QuandoDelecaoFalhar() {
        Long id = 1L;

        // Simula erro ao deletar
        doThrow(new RuntimeException("Erro ao deletar")).when(categoriaService).deletarCategoria(eq(id));

        //  (já que o controller não trata)
        assertThrows(RuntimeException.class, () -> {
            categoriaController.deletarCategoria(id);
        });
    }



}