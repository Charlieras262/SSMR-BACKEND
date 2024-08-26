package com.g5.ssmr.repositories;

import com.g5.ssmr.models.CatalogueChild;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CatalogueChildRepository extends CrudRepository<CatalogueChild, Integer> {


    boolean existsByIdCatalogue(Integer idCatalogue);

    @Override
    List<CatalogueChild> findAll();


    List<CatalogueChild> findAllByIdCatalogue(Integer idCatalogue);
}
