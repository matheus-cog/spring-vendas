package com.matheusguedes.api.controller;

import com.matheusguedes.api.dto.CredenciaisDTO;
import com.matheusguedes.api.dto.TokenDTO;
import com.matheusguedes.domain.entity.Usuario;
import com.matheusguedes.exception.SenhaInvalidaException;
import com.matheusguedes.security.jwt.JwtService;
import com.matheusguedes.service.implementation.UsuarioServiceImplementation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(description = "Esta aba inclui a rota para autenticação.", tags = "Autenticação")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioServiceImplementation usuarioService;

    private final JwtService jwtService;

    @PostMapping
    @ApiOperation("Realiza a autenticação do usuário.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Usuário não encontrado.")
    })
    public TokenDTO auth(@RequestBody @Valid CredenciaisDTO credenciais){
        try {
            Usuario usuario = Usuario.builder()
                    .username(credenciais.getUsername())
                    .senha(credenciais.getSenha())
                    .build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuarioAutenticado.getUsername(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException ex){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

}