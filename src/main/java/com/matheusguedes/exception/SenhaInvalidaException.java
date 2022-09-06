package com.matheusguedes.exception;

import static com.matheusguedes.api.Response.SENHA_INVALIDA;

public class SenhaInvalidaException extends RuntimeException {

    public SenhaInvalidaException() {
        super(SENHA_INVALIDA);
    }

}