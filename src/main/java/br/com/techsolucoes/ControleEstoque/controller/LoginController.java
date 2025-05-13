package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.UsuarioDTO;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.service.UsuarioService;
import br.com.techsolucoes.ControleEstoque.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody br.com.techsolucoes.ControleEstoque.dto.UsuarioLoginDTO loginDTO) {
        boolean autenticado = usuarioService.autenticar(loginDTO.getEmail(), loginDTO.getSenha());
        if (autenticado) {
            String token = jwtUtil.generateToken(loginDTO.getEmail());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
            //return null;
        } else {
            return ResponseEntity.status(401).body("{\"error\": \"Credenciais inválidas\"}");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Optional<Usuario> existente = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(400).body("{\"erro\": \"Email já cadastrado.\"}");
        }

        Usuario novo = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha()) // (ideal: usar BCrypt aqui)
                .perfil(usuarioDTO.getPerfil())
                .build();

        Usuario salvo = usuarioService.salvar(novo);
        return ResponseEntity.ok(salvo);
    }
}
