package com.matheusguedes.service;

import com.matheusguedes.domain.entity.Usuario;
import com.matheusguedes.domain.repository.UsuarioRepository;
import com.matheusguedes.exception.SenhaInvalidaException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.matheusguedes.api.Response.NOME_DE_USUARIO_INVALIDO;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public Usuario salvar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario) {
        var userDetails = loadUserByUsername(usuario.getUsername());
        if (passwordEncoder.matches(usuario.getSenha(), userDetails.getPassword())){
            return userDetails;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOME_DE_USUARIO_INVALIDO));

        var roles = usuario.isAdmin() ? new String[] {"ADMIN", "USER"} : new String[] {"USER"};

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

}