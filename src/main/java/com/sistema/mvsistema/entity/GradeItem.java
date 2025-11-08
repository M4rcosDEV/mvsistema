package com.sistema.mvsistema.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "grade_item")
@Data
public class GradeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;
}
