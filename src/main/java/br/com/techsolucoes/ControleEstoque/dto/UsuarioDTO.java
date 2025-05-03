package br.com.techsolucoes.ControleEstoque.dto;

import br.com.techsolucoes.ControleEstoque.entity.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
}
