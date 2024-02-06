package com.matheusguedes.service;

import com.matheusguedes.api.dto.ItemPedidoDTO;
import com.matheusguedes.api.dto.PedidoDTO;
import com.matheusguedes.domain.entity.Cliente;
import com.matheusguedes.domain.entity.ItemPedido;
import com.matheusguedes.domain.entity.Pedido;
import com.matheusguedes.domain.entity.Produto;
import com.matheusguedes.domain.enums.StatusPedido;
import com.matheusguedes.domain.repository.ClienteRepository;
import com.matheusguedes.domain.repository.ItemPedidoRepository;
import com.matheusguedes.domain.repository.PedidoRepository;
import com.matheusguedes.domain.repository.ProdutoRepository;
import com.matheusguedes.exception.PedidoNaoEncontradoException;
import com.matheusguedes.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.matheusguedes.api.Response.*;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public Pedido salvar(PedidoDTO dto) {
        var cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RegraNegocioException(IDENTIFICADOR_CLIENTE_INVALIDO));

        var pedido = criarPedido(dto, cliente, StatusPedido.REALIZADO);

        var listaItensPedido = converteItens(pedido, dto.getItens());
        pedido.setItens(listaItensPedido);

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(listaItensPedido);

        return pedido;
    }

    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return pedidoRepository.save(pedido);
        }).orElseThrow(PedidoNaoEncontradoException::new);
    }

    private List<ItemPedido> converteItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException(ERROR_PEDIDO_SEM_ITENS);
        }

        return itens.stream().map(dto -> {
            var produto = produtoRepository.findById(dto.getProduto())
                    .orElseThrow(() -> new RegraNegocioException(PRODUTO_CLIENTE_INVALIDO + " " + dto.getProduto()));

            return criarItemPedido(dto, pedido, produto);
        }).collect(Collectors.toList());
    }

    private static Pedido criarPedido(PedidoDTO dto, Cliente cliente, StatusPedido status) {
        return Pedido.builder()
                .total(dto.getTotal())
                .dataPedido(LocalDate.now())
                .cliente(cliente)
                .status(status)
                .build();
    }

    private static ItemPedido criarItemPedido(ItemPedidoDTO dto, Pedido pedido, Produto produto) {
        return ItemPedido.builder()
                .pedido(pedido)
                .quandidade(dto.getQuantidade())
                .produto(produto)
                .build();
    }

}