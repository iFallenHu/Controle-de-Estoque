package br.com.techsolucoes.ControleEstoque.service;


import br.com.techsolucoes.ControleEstoque.DTO.UsuarioRequestDTO;
import br.com.techsolucoes.ControleEstoque.DTO.UsuarioResponseDTO;
import br.com.techsolucoes.ControleEstoque.entity.Usuario;
import br.com.techsolucoes.ControleEstoque.exception.EmailJaCadastradoException;
import br.com.techsolucoes.ControleEstoque.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioResponseDTO salvar(UsuarioRequestDTO usuarioRequestDTO) {
        usuarioRequestDTO.setSenha(passwordEncoder.encode(usuarioRequestDTO.getSenha()));
        Usuario usuario = modelMapper.map(usuarioRequestDTO, Usuario.class);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        UsuarioResponseDTO responseDTO = modelMapper.map(usuarioSalvo, UsuarioResponseDTO.class);
        return responseDTO;
    }

    public boolean autenticar(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if(usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return passwordEncoder.matches(senha, usuario.getSenha());
        }
        return false;

//        System.out.println("INICIANDO A AUTENTICAÇÃO...");
//        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
//        if(usuarioOpt.isPresent()) {
//            Usuario usuario = usuarioOpt.get();
//            System.out.println("USUÁRIO ENCONTRADO: " + usuario.getEmail());
//            return passwordEncoder.matches(senha, usuario.getSenha());
//        }
//        return false;
    }

    public boolean autenticar2(String email, String senha) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, senha)
            );
            return authentication.isAuthenticated();
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    public void verificarEmailDuplicado(String email) {
        Optional<Usuario> existente = usuarioRepository.findByEmail(email);
        if (existente.isPresent()) {
            throw new EmailJaCadastradoException(email);
        }
    }

    public List<UsuarioResponseDTO> listar() {
        return  usuarioRepository.findAll().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado"));

        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado"));

        usuarioRepository.delete(usuario);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado"));

        // Atualiza apenas os campos permitidos
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setPerfil(dto.getPerfil());

        Usuario atualizado = usuarioRepository.save(usuario);
        return modelMapper.map(atualizado, UsuarioResponseDTO.class);
    }
}
