package com.sistema.mvsistema.dto;

public class ClienteBusca {
    private final Long id;
    private final String nome;
    private final String cpfCnpj;
    private final String tipoPessoa;
    private final String telefone;

    public ClienteBusca(Long id, String nome, String cpfCnpj, String tipoPessoa, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.tipoPessoa = tipoPessoa;
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public String getTelefone() {
        return telefone;
    }

}
