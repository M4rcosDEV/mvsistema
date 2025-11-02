package com.sistema.mvsistema.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
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

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnp) {
        this.cpfCnpj = cpfCnp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getInscricaoEst() {
        return inscricaoEst;
    }

    public void setInscricaoEst(String inscricaoEst) {
        this.inscricaoEst = inscricaoEst;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
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
                ", criadoEm=" + criadoEm +
                ", ativo=" + ativo +
                '}';
    }
}
