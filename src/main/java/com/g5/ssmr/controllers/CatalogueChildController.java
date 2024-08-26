package com.g5.ssmr.controllers;

import com.g5.ssmr.models.CatalogueChild;
import com.g5.ssmr.services.CatalogueChildService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class CatalogueChildController {

    @Autowired
    CatalogueChildService catalogueChildService;

    @GetMapping(value="external/catalogue/getAll")
    @ApiOperation(value="obtiene todos los catalogos hijos registrados")
    public List<CatalogueChild> getAllCatalogueChild(){
        return catalogueChildService.getAllCatalogueChild();
    }

    @GetMapping(value = "external/catalogue/getBy/{id}")
    @ApiOperation(value="obtiene todos los catalogos hijos segun el id del padre")
    public  List<CatalogueChild> getAllCatalogueById(@PathVariable @ApiParam(value = "id del catalogo padre")Integer id){
        return catalogueChildService.getAllCatlaogueChildByParent(id);
    }
}
