package com.sistema.mvsistema.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "municipio")
@Data
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer codigo;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 2)
    private String uf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

}
