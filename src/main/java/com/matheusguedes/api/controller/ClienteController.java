package com.matheusguedes.api.controller;

import com.matheusguedes.domain.entity.Cliente;
import com.matheusguedes.domain.repository.ClienteRepository;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static com.matheusguedes.api.Response.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Api(description = "Esta aba inclui todas as operações a serem realizadas a um cliente.", tags = "Clientes")
public class ClienteController {

    private final ClienteRepository repository;

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um único cliente.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado."),
            @ApiResponse(code = 404, message = "Cliente não encontrado."),
    })
    public Cliente findById(@PathVariable("id") @ApiParam("ID do cliente.") Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));
    }

    @GetMapping
    @ApiOperation("Obter detalhes de todos os clientes com o filtro aplicado.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado.")
    })
    public List<Cliente> find(Cliente cliente) {
        var matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return repository.findAll(Example.of(cliente, matcher));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Cria um novo cliente.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente criado com sucesso!")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return repository.save(cliente);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Altera um único cliente por completo.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente alterado com sucesso!"),
            @ApiResponse(code = 404, message = "Cliente não encontrado."),
    })
    public void update(@PathVariable Integer id, @RequestBody @Valid Cliente cliente) {
        repository.findById(id).map(clienteSalvo -> {
            cliente.setId(clienteSalvo.getId());
            return repository.save(cliente);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_DURANTE_OPERACAO));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um único cliente.")
    @ApiResponses(@ApiResponse(code = 204, message = ""))
    public void delete(@PathVariable Integer id) {
        repository.findById(id).map(cliente -> {
            repository.delete(cliente);
            return cliente;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_DURANTE_OPERACAO));
    }

}