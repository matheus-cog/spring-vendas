package com.matheusguedes.domain.enums;

import java.util.Arrays;

public enum StatusPedido {

    REALIZADO,
    CANCELADO;

    public static StatusPedido findByName(String name) {
        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}