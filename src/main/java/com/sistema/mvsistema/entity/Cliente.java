package com.sistema.mvsistema.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String nome;

    @Column(length = 30)
    private String sobrenome;

    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    @Column(name = "tipo_pessoa", nullable = false, length = 1)
    private String tipoPessoa;

    @Column(name = "cpf_cnpj", length = 20)
    private String cpfCnpj;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "estado_civil", length = 1)
    private String estadoCivil;

    @Column(length = 1)
    private String genero;

    @Column(length = 20)
    private String registro;

    @Column(name = "iscricao_est", length = 20)
    private String inscricaoEst;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "observacao", columnDefinition = "TEXT")
    private String observacao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "cliente", // 1. "cliente" é o nome do campo na classe Endereco
            cascade = CascadeType.ALL, // 2. Salva/Atualiza/Remove endereços junto com o cliente
            orphanRemoval = true, // 3. Remove endereços do banco se forem removidos da lista
            fetch = FetchType.LAZY
    )
    private List<Endereco> enderecos = new ArrayList<>();

    public void addEndereco(Endereco endereco){
        this.enderecos.add(endereco);
        endereco.setCliente(this);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", nomeFantasia='" + nomeFantasia + '\'' +
                ", tipoPessoa='" + tipoPessoa + '\'' +
                ", cpfCnpj='" + cpfCnpj + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", genero='" + genero + '\'' +
                ", registro='" + registro + '\'' +
                ", inscricaoEst='" + inscricaoEst + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", observacao='" + observacao + '\'' +
                ", ativo=" + ativo +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
