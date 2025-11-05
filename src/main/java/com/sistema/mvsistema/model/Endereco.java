package com.sistema.mvsistema.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "endereco")
@Data
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String tipoEndereco;

    @Column(length = 10)
    private String cep;

    @Column(length = 150)
    private String rua;

    @Column(length = 20)
    private String numero;

    @Column(length = 100)
    private String complemento;

    @Column(length = 50)
    private String bairro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipio_id", nullable = false)
    private Municipio municipio;

    @Column(length = 2, nullable = false)
    private String estado;

    @Column(length = 20)
    private String pais;

    @Column(name = "ibge_code", length = 10)
    private String ibgeCodigo;

}
