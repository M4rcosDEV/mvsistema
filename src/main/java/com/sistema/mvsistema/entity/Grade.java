package com.sistema.mvsistema.entity;

import com.sistema.mvsistema.repository.GradeRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 50, nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GradeItem> gradeItens = new ArrayList<>();

    public void addGradeItem(GradeItem gradeItem){
        this.gradeItens.add(gradeItem);
        gradeItem.setGrade(this);
    }

    public Grade(Integer id, String nome, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }
}
