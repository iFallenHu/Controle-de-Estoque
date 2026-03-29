package br.com.techsolucoes.ControleEstoque.DTO;

import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "João da Silva")
    private String nome;

    @Schema(example = "joao@email.com")
    private String email;

    @Schema(example = "ADMIN")
    private Perfil perfil;
}
