package com.sistema.mvsistema.dto;

import lombok.Data;

@Data
public class ClienteBusca {
    private final Long id;
    private final String nome;
    private final String cpfCnpj;
    private final String tipoPessoa;
    private final String telefone;

}
