package com.g5.ssmr.repositories;

import com.g5.ssmr.models.Requirement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequirementRepository extends CrudRepository<Requirement, Integer> {

    void deleteByMarcoRegulatorio(Integer marcoRegulatorio);
    List<Requirement> findByMarcoRegulatorio(Integer marcoRegulatorio);
}