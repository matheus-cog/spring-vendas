package com.matheusguedes.service.implementation;

import com.matheusguedes.domain.entity.Usuario;
import com.matheusguedes.domain.repository.UsuarioRepository;
import com.matheusguedes.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.matheusguedes.api.Response.NOME_DE_USUARIO_INVALIDO;

@Service
public class UsuarioServiceImplementation implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    public UserDetails autenticar(Usuario usuario){
        UserDetails userDetails = loadUserByUsername(usuario.getUsername());
        if(passwordEncoder.matches(usuario.getSenha(), userDetails.getPassword())){
            return userDetails;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOME_DE_USUARIO_INVALIDO));

        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

}