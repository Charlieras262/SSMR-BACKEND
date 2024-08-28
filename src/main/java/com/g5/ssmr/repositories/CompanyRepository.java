package com.g5.ssmr.repositories;

import com.g5.ssmr.models.Company;
import com.g5.ssmr.projections.CompanyDetailProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company, Integer> {
    @Override
    List<Company> findAll();

    @Query(value = "select e.id, e.nombre, e.descripcion, u.\"name\" as representante from g5_ssmr.empresas e \n" +
            "inner join g5_ssmr.users u on e.representante = u.id_user \n", nativeQuery = true)
    List<CompanyDetailProjection> getAll();

    @Query(value = "select e.id, e.nombre, e.descripcion, u.\"name\" as representante from g5_ssmr.empresas e \n" +
            "inner join g5_ssmr.users u on e.representante = u.id_user \n" +
            "where e.id = :company", nativeQuery = true)
    Optional<CompanyDetailProjection> getById(@Param("company") int company);
}