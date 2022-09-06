package com.matheusguedes.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "cliente") // Redundancia, quando o nome da tabela é igual ao da entidade, não é necessário a anotação @Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Redundancia, quando o nome do atr é igual ao do banco, não é necessário a anotação @Column
    private Integer id;

    @Column(name = "nome", length = 200)
    @NotEmpty(message = "{erro.you-must-insert-name}")
    private String nome;

    @Column(name = "cpf", length = 11, unique = true)
    @NotEmpty(message = "{erro.you-must-insert-cpf}")
    @CPF(message = "{erro.invalid-cpf}")
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<Pedido> pedidos;

}
