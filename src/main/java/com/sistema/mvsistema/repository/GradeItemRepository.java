package com.sistema.mvsistema.repository;

import com.sistema.mvsistema.entity.GradeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeItemRepository extends JpaRepository<GradeItem, Integer> {
}
