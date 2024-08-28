package com.g5.ssmr.repositories;

import com.g5.ssmr.models.AuditResult;
import com.g5.ssmr.projections.AuditDetailProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditResultRepository extends CrudRepository<AuditResult, Integer> {
    @Override
    List<AuditResult> findAll();

    void deleteAllByAuditoria(Integer auditoria);

    // Find result status by audit and requirement
    @Query(value = "select ar.id_resultado from g5_ssmr.auditoria_resultado ar\n" +
            "where ar.id_auditoria = :auditoria and ar.id_requisitos = :requisito", nativeQuery = true)
    Integer findByAuditoriaAndRequisito(@Param("auditoria") Integer auditoria, @Param("requisito") Integer requisito);
}