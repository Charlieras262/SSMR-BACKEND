package com.g5.ssmr.repositories;

import com.g5.ssmr.models.Audit;
import com.g5.ssmr.projections.AuditDetailProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuditRepository extends CrudRepository<Audit, Integer> {
    @Override
    List<Audit> findAll();

    @Query(value = "select a.id, e.nombre as empresa, mr.nombre as \"marcoRegulatorio\", cc.\"name\" as estado, a.observaciones, concat(u.\"name\",' ', u.last_name) \"nombreAuditor\" from g5_ssmr.auditoria a \n" +
            "inner join g5_ssmr.empresas e on a.id_empresa = e.id\n" +
            "inner join g5_ssmr.marcos_regulatorios mr on a.id_marco_regulatorio =mr.id\n" +
            "inner join g5_ssmr.catalogue_child cc on a.id_estado = cc.id_catalogue_child\n" +
            "inner join g5_ssmr.users u on a.auditor = u.id_user \n" +
            " where a.auditor = :username", nativeQuery = true)
    List<AuditDetailProjection> getAllUser(@Param("username") String username);

    @Query(value = "select a.id, e.nombre as empresa, mr.nombre as \"marcoRegulatorio\", cc.\"name\" as estado, a.observaciones from g5_ssmr.auditoria a \n" +
            "inner join g5_ssmr.empresas e on a.id_empresa = e.id\n" +
            "inner join g5_ssmr.marcos_regulatorios mr on a.id_marco_regulatorio =mr.id\n" +
            "inner join g5_ssmr.catalogue_child cc on a.id_estado = cc.id_catalogue_child\n" +
            "where e.representante = :username", nativeQuery = true)
    List<AuditDetailProjection> getAll(@Param("username") String username);

    @Query(value = "select a.id, e.nombre as empresa, mr.nombre as \"marcoRegulatorio\", cc.\"name\" as estado, a.observaciones, concat(u.\"name\",' ', u.last_name) \"nombreAuditor\" from g5_ssmr.auditoria a \n" +
            "inner join g5_ssmr.empresas e on a.id_empresa = e.id\n" +
            "inner join g5_ssmr.marcos_regulatorios mr on a.id_marco_regulatorio =mr.id\n" +
            "inner join g5_ssmr.catalogue_child cc on a.id_estado = cc.id_catalogue_child\n" +
            "inner join g5_ssmr.users u on a.auditor = u.id_user \n" +
            "where a.id = :id", nativeQuery = true)
    Optional<AuditDetailProjection> getById(@Param("id") Integer id);
}