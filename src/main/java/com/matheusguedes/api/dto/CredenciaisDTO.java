package com.matheusguedes.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CredenciaisDTO {

    @NotEmpty(message = "{erro.invalid-user}")
    private String username;

    @NotEmpty(message = "{erro.invalid-pass}")
    private String senha;

}