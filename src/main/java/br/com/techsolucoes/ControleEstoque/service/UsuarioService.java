package br.com.techsolucoes.ControleEstoque.service;


import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario salvar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String email, String senha) {
//        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
//        if(usuarioOpt.isPresent()) {
//            Usuario usuario = usuarioOpt.get();
//            return passwordEncoder.matches(senha, usuario.getSenha());
//        }
//        return false;

        System.out.println("INICIANDO A AUTENTICAÇÃO...");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if(usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("USUÁRIO ENCONTRADO: " + usuario.getEmail());
            return passwordEncoder.matches(senha, usuario.getSenha());
        }
        return false;
    }

}
