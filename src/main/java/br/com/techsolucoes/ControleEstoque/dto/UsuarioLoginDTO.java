package br.com.techsolucoes.ControleEstoque.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLoginDTO {
    private String email;
    private String senha;
}
