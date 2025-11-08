package com.sistema.mvsistema.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "regiao")
public class Regiao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 50)
    private String nome;
}
