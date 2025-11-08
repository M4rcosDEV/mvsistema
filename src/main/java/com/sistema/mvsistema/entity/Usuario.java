package com.sistema.mvsistema.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(name = "nivel_acesso", length = 20)
    private String nivelAcesso;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;

    public Usuario() {
    }

    public Usuario(String nome, String senha, String nivelAcesso, LocalDateTime criadoEm, Boolean ativo) {
        this.nome = nome;
        this.senha = senha;
        this.nivelAcesso = nivelAcesso;
        this.criadoEm = criadoEm;
        this.ativo = ativo;
    }
}
