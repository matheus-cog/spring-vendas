package com.matheusguedes.service;

import com.matheusguedes.api.dto.PedidoDTO;
import com.matheusguedes.domain.entity.Pedido;
import com.matheusguedes.domain.enums.StatusPedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizaStatus(Integer id, StatusPedido statusPedido);
    
}
