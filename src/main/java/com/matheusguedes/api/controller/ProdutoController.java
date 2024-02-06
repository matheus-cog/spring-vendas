package com.matheusguedes.api.controller;

import com.matheusguedes.api.Response;
import com.matheusguedes.domain.entity.Produto;
import com.matheusguedes.domain.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
@Api(description = "Esta aba inclui todas as operações a serem realizadas a um produto.", tags = "Produtos")
public class ProdutoController {

    private final ProdutoRepository repository;

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um único produto.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Produto não encontrado.")
    })
    public Produto findById(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Response.PRODUTO_NAO_ENCONTRADO));
    }

    @GetMapping
    @ApiOperation("Obter detalhes de todos os produtos com o filtro aplicado.")
    @ApiResponse(code = 200, message = "OK")
    public List<Produto> find(Produto produto) {
        var matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return repository.findAll(Example.of(produto, matcher));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salva um novo produto.")
    @ApiResponse(code = 201, message = "")
    public Produto save(@RequestBody @Valid Produto produto) {
        return repository.save(produto);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza um produto.")
    @ApiResponse(code = 204, message = "")
    public void update(@PathVariable Integer id, @RequestBody @Valid Produto produto) {
        repository.findById(id).map(produtoSalvo -> {
            produto.setId(produtoSalvo.getId());
            return repository.save(produto);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Response.ERROR_DURANTE_OPERACAO));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um produto.")
    @ApiResponse(code = 204, message = "")
    public void delete(@PathVariable Integer id) {
        repository.findById(id).map(produto -> {
            repository.delete(produto);
            return produto;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, Response.ERROR_DURANTE_OPERACAO));
    }

}