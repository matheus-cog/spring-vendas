package com.matheusguedes.domain.repository;

import com.matheusguedes.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findByNomeLike(String nome);

    @Query(value = "SELECT C FROM Cliente C WHERE C.nome LIKE :nome")
    List<Cliente> encontrarPorNomeJPQL(@Param("nome") String nome);

    @Query(value = "SELECT * FROM cliente WHERE nome LIKE %:nome%", nativeQuery = true)
    List<Cliente> encontrarPorNome(@Param("nome") String nome);

    List<Cliente> findByNomeOrId(String nome, Integer id);

    @Query("DELETE FROM Cliente C WHERE C.nome = :nome")
    @Modifying
    void deleteByNome(String nome);

    boolean existsByNome(String nome);

    @Query("SELECT C from Cliente C LEFT JOIN FETCH C.pedidos WHERE C.id = :id")
    Cliente findClienteFetchPedidos(@Param("id") Integer id);
}