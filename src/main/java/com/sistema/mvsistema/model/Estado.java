package com.sistema.mvsistema.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_uf", nullable = false)
    private Integer codigoUf;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 2)
    private String sigla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regiao_id", nullable = false)
    private Regiao regiao;

}
