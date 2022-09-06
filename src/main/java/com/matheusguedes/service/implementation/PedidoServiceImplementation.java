package com.matheusguedes.service.implementation;

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
import com.matheusguedes.service.PedidoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.matheusguedes.api.Response.*;

@Service
@AllArgsConstructor
public class PedidoServiceImplementation implements PedidoService {

    private PedidoRepository pedidoRepository;
    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RegraNegocioException(IDENTIFICADOR_CLIENTE_INVALIDO));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> list = converteItens(pedido, dto.getItens());
        pedido.setItens(list);

        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(list);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return pedidoRepository.save(pedido);
        }).orElseThrow(PedidoNaoEncontradoException::new);
    }

    private List<ItemPedido> converteItens(Pedido pedido, List<ItemPedidoDTO> itens){
        if(itens.isEmpty()){
            throw new RegraNegocioException(ERROR_PEDIDO_SEM_ITENS);
        }

        return itens.stream().map(dto -> {
            Produto produto = produtoRepository.findById(dto.getProduto())
                    .orElseThrow(() -> new RegraNegocioException(PRODUTO_CLIENTE_INVALIDO+" "+dto.getProduto()));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setQuandidade(dto.getQuantidade());
            item.setProduto(produto);
            return item;
        }).collect(Collectors.toList());
    }
}
