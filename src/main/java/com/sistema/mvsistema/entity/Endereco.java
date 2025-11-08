package com.sistema.mvsistema.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "endereco")
@Getter
@Setter
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

    @Column(nullable = false)
    private Boolean ativo = true;

    @Override
    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", nome='" + nome + '\'' +
                ", tipoEndereco='" + tipoEndereco + '\'' +
                ", cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", bairro='" + bairro + '\'' +
                ", municipio=" + municipio +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", ibgeCodigo='" + ibgeCodigo + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
