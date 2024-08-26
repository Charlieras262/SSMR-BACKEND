package com.g5.ssmr.services;

import com.g5.ssmr.models.CatalogueChild;
import com.g5.ssmr.repositories.CatalogueChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CatalogueChildService {

    @Autowired
    CatalogueChildRepository catalogueChildRepository;

    public List<CatalogueChild> getAllCatalogueChild() {
        List<CatalogueChild> catalogueChildList = catalogueChildRepository.findAll();

        if (catalogueChildList.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existen catalogos");

        }
        return catalogueChildList;
    }

    public List<CatalogueChild> getAllCatlaogueChildByParent(Integer idCatalogue) {

        if (!catalogueChildRepository.existsByIdCatalogue(idCatalogue)) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Existen registros para este catalogo");


        }
        List<CatalogueChild> catalogueChildList = catalogueChildRepository.findAllByIdCatalogue(idCatalogue);

        if (catalogueChildList.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existen catalogos");

        }
        return catalogueChildList;

    }
}
