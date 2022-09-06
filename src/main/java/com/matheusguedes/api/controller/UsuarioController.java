package com.matheusguedes.api.controller;

import com.matheusguedes.domain.entity.Usuario;
import com.matheusguedes.service.implementation.UsuarioServiceImplementation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Api(description = "Esta aba inclui todas as operações a serem realizadas a um usuário.", tags = "Usuários")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImplementation usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cria um novo usuário.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "")
    })
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        return usuarioService.salvar(usuario);
    }

}