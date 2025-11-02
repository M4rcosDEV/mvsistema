package com.sistema.mvsistema.dto;

import com.sistema.mvsistema.model.Municipio;

public class EnderecoDto {
    private final Long id;
    private final String nome;
    private final String tipoEndereco;
    private final String cep;
    private final String municipio;

    public EnderecoDto(Long id, String nome, String tipoEndereco, String cep, Municipio municipio) {
        this.id = id;
        this.nome = nome;
        this.tipoEndereco = tipoEndereco;
        this.cep = cep;
        this.municipio = municipio.getNome();
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getTipoEndereco() {
        return tipoEndereco;
    }

    public String getCep() {
        return cep;
    }

    public String getNome() {
        return nome;
    }

    public Long getId() {
        return id;
    }
}
