package com.sistema.mvsistema.repository;

import com.sistema.mvsistema.model.Regiao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegiaoRepository extends JpaRepository<Regiao, Integer> {
}
