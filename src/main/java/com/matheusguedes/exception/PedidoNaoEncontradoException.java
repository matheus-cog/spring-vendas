package com.matheusguedes.exception;

import static com.matheusguedes.api.Response.PEDIDO_NAO_ENCONTRADO;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super(PEDIDO_NAO_ENCONTRADO);
    }

}