package br.com.techsolucoes.ControleEstoque.controller;

import br.com.techsolucoes.ControleEstoque.DTO.UsuarioLoginDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioResponseDTO;
import br.com.techsolucoes.ControleEstoque.security.jwt.JwtService;
import br.com.techsolucoes.ControleEstoque.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @Operation(summary = "Login de usuários", description = "Retorna um token JWT caso o login tenha sucesso.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDTO loginDTO) {
        boolean autenticado = usuarioService.autenticar2(loginDTO.getEmail(), loginDTO.getSenha()); //JwtService
        if (autenticado) {
            String token = jwtService.generateToken(loginDTO.getEmail());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
            //return null;
        } else {
            return ResponseEntity.status(401).body("{\"error\": \"Credenciais inválidas\"}");
        }
    }



    @Operation(summary = "Registrar um novo usuário", description = "Retorna o novo usuário cadastrado.")
//    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = UsuarioResponseDTO.class)))
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        usuarioService.verificarEmailDuplicado(usuarioRequestDTO.getEmail());

        UsuarioResponseDTO responseDTO = usuarioService.salvar(usuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
