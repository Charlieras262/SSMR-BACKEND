package com.g5.ssmr.repositories;

import com.g5.ssmr.models.RegulatoryFramework;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegulatoryFrameworkRepository extends CrudRepository<RegulatoryFramework, Integer> {

    @Override
    List<RegulatoryFramework> findAll();
}