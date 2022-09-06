package com.matheusguedes.domain.enums;

public enum StatusPedido {

    REALIZADO,
    CANCELADO;

    public static StatusPedido findByName(String name) {
        StatusPedido result = null;
        for (StatusPedido status : values()) {
            if (status.name().equalsIgnoreCase(name)) {
                result = status;
                break;
            }
        }
        return result;
    }
}