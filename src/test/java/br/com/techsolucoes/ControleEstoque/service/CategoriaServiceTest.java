    package br.com.techsolucoes.ControleEstoque.service;

    import br.com.techsolucoes.ControleEstoque.DTO.CategoriaDTO;
    import br.com.techsolucoes.ControleEstoque.entity.Categoria;
    import br.com.techsolucoes.ControleEstoque.repository.CategoriaRepository;
    import org.junit.jupiter.api.Test;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.Mockito;
    import org.mockito.junit.jupiter.MockitoExtension;

    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;
    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class CategoriaServiceTest {

        @Mock
        private CategoriaRepository categoriaRepository;

        @InjectMocks
        private CategoriaService categoriaService;

        @Test
        void deveRetornarCategoriaQuandoIdExistir() {

            //Arrange
            Long id = 1L;

            Categoria categoriaMock = new Categoria();
            categoriaMock.setId(id);
            categoriaMock.setNome("Eletrônicos");

            when(categoriaRepository.findById(id))
                    .thenReturn(Optional.of(categoriaMock));

            // Act
            Categoria resultado = categoriaService.buscarPorId(id);

            // Assert
            assertNotNull(resultado);
            assertEquals("Eletrônicos", resultado.getNome());
            assertEquals(id, resultado.getId());
        }

        @Test
        void deveLancarExcecaoQuandoIdNaoExistir() {
            // Arrange
            Long id = 999L;
            when(categoriaRepository.findById(id))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class, () -> {
                categoriaService.buscarPorId(id);
            });
        }

        @Test
        void deveSalvarCategoriaComSucesso() {
            // Arrange
            CategoriaDTO novaCategoria = new CategoriaDTO();
            novaCategoria.setNome("Informática");

            Categoria categoriaSalva = new Categoria();
            categoriaSalva.setId(1L);
            categoriaSalva.setNome("Informática");

            when(categoriaRepository.save(any(Categoria.class)))
                    .thenReturn(categoriaSalva);

            // Act
            Categoria resultado = categoriaService.salvarCategoria(novaCategoria);

            // Assert
            assertNotNull(resultado);
            assertEquals("Informática", resultado.getNome());
        }
        @Test
        void deveLancarExcecaoQuandoCategoriaForNull(){
            assertThrows(IllegalArgumentException.class, () -> {
               categoriaService.salvarCategoria(null);
            });

        }

        @Test
        void deveListarCategoriasComSucesso(){
            //Arrange
            Categoria cat1 = new Categoria();
            cat1.setId(1L);
            cat1.setNome("Informática");

            Categoria cat2 =new Categoria();
            cat2.setId(2L);
            cat2.setNome("Alimentos");

            List<Categoria> categorias = Arrays.asList(cat1, cat2);

            when(categoriaRepository.findAll()).thenReturn(categorias);

            //Act
            List<Categoria> resultado = categoriaService.listarCategoria();

            //Assert
            assertNotNull(resultado);
            assertEquals(2,resultado.size());
            assertEquals("Informática", resultado.get(0).getNome());
            assertEquals("Alimentos", resultado.get(1).getNome());
        }

        @Test
        void deveLancarExcecaoQuandoNaoExistiremCategorias(){
            //Arrange
            when(categoriaRepository.findAll()).thenReturn(Collections.emptyList());

            //Act & Assert
            assertThrows(IllegalStateException.class, () -> categoriaService.listarCategoria());
        }

        @Test
        void deveAtualizarCategoriasComSucesso(){
            // Arrange
            long id = 1L;
            Categoria categoriaExistente = new Categoria();
            categoriaExistente.setId(id);
            categoriaExistente.setNome("Antigo Nome");

            CategoriaDTO dto = new CategoriaDTO();
            dto.setNome("Novo Nome");

            when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaExistente));
            when(categoriaRepository.save(Mockito.<Categoria>any())).thenAnswer(invocation -> invocation.getArgument(0));


            // Act
            Categoria categoriaAtualizada = categoriaService.atualizarCategoria(id, dto);

            // Assert
            assertNotNull(categoriaAtualizada);
            assertEquals("Novo Nome", categoriaAtualizada.getNome());
            verify(categoriaRepository).findById(id);
            verify(categoriaRepository).save(categoriaExistente);
        }

        @Test
        void DeveLancarexcecaoQuandoNaoAtualizar(){
            // Arrange
            long id = 1L;
            CategoriaDTO dto = new CategoriaDTO();
            dto.setNome("Qualquer Nome");

            when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

            // Act
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                categoriaService.atualizarCategoria(id, dto);
            });

            //Assert
            assertEquals("Categoria não encontrada", exception.getMessage());
            verify(categoriaRepository).findById(id);
            verify(categoriaRepository, never()).save(Mockito.<Categoria>any());
        }

        @Test
        void DeveDeletarCategoriasComSucesso(){

            //Arrange
            Long id = 1L;
            Categoria categoriaExistente = new Categoria();
            categoriaExistente.setId(id);
            categoriaExistente.setNome("Categoria Teste");

            when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaExistente));

            //Act
            categoriaService.deletarCategoria(id);

            //Assert
            verify(categoriaRepository).findById(id);
            verify(categoriaRepository).deleteById(id);
        }

        @Test
        void DeveLancarExcecaoQuandoNaoEncontrarCategoria(){

            //Arrange
            Long id = 1L;
            when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

            //Act
            RuntimeException exception = assertThrows(RuntimeException.class, () ->{
                categoriaService.deletarCategoria(id);
            });

            //Assert
            assertEquals("Categoria não encontrada", exception.getMessage());
            verify(categoriaRepository).findById(id);
            verify(categoriaRepository, never()).delete(Mockito.<Categoria>any());

        }
    }
