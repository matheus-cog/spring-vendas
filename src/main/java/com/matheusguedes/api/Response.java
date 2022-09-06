package com.matheusguedes.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.List;

public class Response {

    @Value("${erro.customer-not-found}")
    public static String CLIENTE_NAO_ENCONTRADO;

    @Value("${erro.product-not-found}")
    public static String PRODUTO_NAO_ENCONTRADO;

    @Value("${erro.invoice-not-found}")
    public static String PEDIDO_NAO_ENCONTRADO;

    @Value("${erro.error-during-proccess}")
    public static String ERROR_DURANTE_OPERACAO;

    @Value("${erro.invalid-customer-id}")
    public static String IDENTIFICADOR_CLIENTE_INVALIDO;

    @Value("${erro.invalid-product-with-id}")
    public static String PRODUTO_CLIENTE_INVALIDO;

    @Value("${erro.your-invoice-must-contain-items}")
    public static String ERROR_PEDIDO_SEM_ITENS;

    @Value("${erro.username-not-found}")
    public static String NOME_DE_USUARIO_INVALIDO;

    @Value("${erro.invalid-password}")
    public static String SENHA_INVALIDA;

    public static final String LISTA_NAO_PODE_SER_VAZIA = "The list cannot be empty.";

    @Getter
    private final List<String> errors;

    public Response(String mensagemErro) {
        this.errors = Collections.singletonList(mensagemErro);
    }

    public Response(List<String> errors){
        this.errors = errors;
    }

}