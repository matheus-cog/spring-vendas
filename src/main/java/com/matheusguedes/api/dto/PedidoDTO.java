package com.matheusguedes.api.dto;

import com.matheusguedes.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    @NotNull(message = "{erro.invalid-customer-code}")
    private Integer idCliente;

    @NotNull(message = "{erro.invalid-total-value}")
    private BigDecimal total;

    @NotEmptyList(message = "{erro.invalid-invoice-items}")
    private List<ItemPedidoDTO> itens;

}