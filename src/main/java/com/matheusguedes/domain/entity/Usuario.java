package com.matheusguedes.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotEmpty(message = "{erro.invalid-user}")
    private String username;

    @Column
    @NotEmpty(message = "{erro.invalid-pass}")
    private String senha;

    @NotNull(message = "{erro.user-is-admin}")
    private boolean admin;

}
