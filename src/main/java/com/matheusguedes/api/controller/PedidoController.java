package com.matheusguedes.api.controller;

import com.matheusguedes.api.dto.AtualizacaoStatusPedidoDTO;
import com.matheusguedes.api.dto.InformacoesItensPedidoDTO;
import com.matheusguedes.api.dto.InformacoesPedidoDTO;
import com.matheusguedes.api.dto.PedidoDTO;
import com.matheusguedes.domain.entity.ItemPedido;
import com.matheusguedes.domain.entity.Pedido;
import com.matheusguedes.domain.enums.StatusPedido;
import com.matheusguedes.exception.RegraNegocioException;
import com.matheusguedes.service.PedidoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.matheusguedes.api.Response.ERROR_DURANTE_OPERACAO;
import static com.matheusguedes.api.Response.PEDIDO_NAO_ENCONTRADO;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/pedidos")
@Api(description = "Esta aba inclui todas as operações a serem realizadas a um pedido.", tags = "Pedidos")
@AllArgsConstructor
public class PedidoController {

    private PedidoService service;

    @PostMapping
    @ResponseStatus(CREATED)
    @ApiOperation("Salva um novo pedido.")
    @ApiResponse(code = 201, message = "")
    public Integer save(@RequestBody @Valid PedidoDTO dto){
        return service.salvar(dto).getId();
    }

    @GetMapping("{id}")
    @ApiOperation("Obter detalhes de um único pedido.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Pedido não encontrado.")
    })
    public InformacoesPedidoDTO findById(@PathVariable Integer id){
        return service.obterPedidoCompleto(id)
                .map(this::converter)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, PEDIDO_NAO_ENCONTRADO));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Atualiza status de um único pedido.")
    @ApiResponse(code = 200, message = "OK")
    public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusPedidoDTO novoStatus){
        if(StatusPedido.findByName(novoStatus.getNovoStatus()) == null){
            throw new RegraNegocioException(ERROR_DURANTE_OPERACAO);
        }
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus.getNovoStatus()));
    }

    private InformacoesPedidoDTO converter(Pedido pedido){
        return InformacoesPedidoDTO.builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nome(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InformacoesItensPedidoDTO> converter(List<ItemPedido> itens){
        if(CollectionUtils.isEmpty(itens)){
            return Collections.emptyList();
        }
        return itens.stream().map(item ->
                InformacoesItensPedidoDTO.builder()
                        .descricao(item.getProduto().getDescricao())
                        .quantidade(item.getQuandidade())
                        .precoUnitario(item.getProduto().getPreco())
                        .build()
        ).collect(Collectors.toList());
    }

}