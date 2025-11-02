package com.sistema.mvsistema.repository;

import com.sistema.mvsistema.dto.ClienteBusca;
import com.sistema.mvsistema.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT new com.sistema.mvsistema.dto.ClienteBusca(c.id, c.nome, c.cpfCnpj, " +
            "(CASE c.tipoPessoa "+
            "   WHEN 'F' THEN 'Física' " +
            "   WHEN 'J' THEN 'Jurídica' " +
            "   WHEN 'P' THEN 'Produtor' " +
            "   WHEN 'I' THEN 'Instituição' " +
            "END)," +
            "c.telefone) " +
            "FROM Cliente c " +
            "WHERE (:nome IS NULL OR :nome = '' OR lower(c.nome) LIKE lower(CONCAT('%', :nome, '%'))) " +
            "AND (:tipoPessoa IS NULL OR :tipoPessoa = '' OR c.tipoPessoa = :tipoPessoa) " +
            "AND (:cpfCnpj IS NULL OR :cpfCnpj = '' OR c.cpfCnpj LIKE CONCAT('%', :cpfCnpj, '%'))")
    Page<ClienteBusca> buscarClientesComFiltros(
            @Param("nome") String nome,
            @Param("tipoPessoa") String tipoPessoa,
            @Param("cpfCnpj") String cpfCnpj,
            Pageable pageable
    );

    List<ClienteBusca> findByNomeStartsWith(String nome);
}
