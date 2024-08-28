package com.g5.ssmr.controllers;

import com.g5.ssmr.dto.CompanyDto;
import com.g5.ssmr.dto.RegulatoryFrameworkDto;
import com.g5.ssmr.models.Company;
import com.g5.ssmr.services.CompanyService;
import com.g5.ssmr.services.RegulatoryFrameworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class RegulatoryFrameworkController {

    @Autowired
    RegulatoryFrameworkService regulatoryFrameworkService;

    @PostMapping("internal/regulatory/framework")
    @ApiOperation(value = "Crea un nuevo marco regulatorio")
    public void createRegulatoryFramework(@ApiParam(value = "Información del marco regulatorio") @RequestBody RegulatoryFrameworkDto regulatoryFramework) {
        regulatoryFrameworkService.createRegulatoryFramework(regulatoryFramework);
    }

    @GetMapping("internal/regulatory/framework")
    @ApiOperation(value = "Retorna todos los marcos regulatorios registrados")
    public List<RegulatoryFrameworkDto> getAllRegulatoryFrameworks() {
        return regulatoryFrameworkService.getAllRegulatoryFrameworks();
    }

    @GetMapping("internal/regulatory/framework/{id}")
    @ApiOperation(value = "Retorna un marco regulatorio registrado en función del id")
    public RegulatoryFrameworkDto getRegulatoryFramework(@PathVariable int id) {
        return regulatoryFrameworkService.getRegulatoryFrameworkById(id);
    }

    @PutMapping("internal/regulatory/framework/{id}")
    @ApiOperation(value = "Actualiza la información de un marco regulatorio")
    public void updateRegulatoryFramework(@PathVariable int id, @ApiParam(value = "Información del marco regulatorio") @RequestBody RegulatoryFrameworkDto regulatoryFramework) {
        regulatoryFrameworkService.updateRegulatoryFramework(id, regulatoryFramework);
    }

    @DeleteMapping("internal/regulatory/framework/{id}")
    @ApiOperation(value = "Elimina un marco regulatorio y todos los requisitos asociados")
    public void deleteRegulatoryFramework(@PathVariable int id) {
        regulatoryFrameworkService.deleteRegulatoryFrameworkById(id);
    }
}
