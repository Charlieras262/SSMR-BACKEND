package com.g5.ssmr.controllers;

import com.g5.ssmr.dto.CompanyDto;
import com.g5.ssmr.models.Company;
import com.g5.ssmr.projections.CompanyDetailProjection;
import com.g5.ssmr.services.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping(value = "internal/company")
    @ApiOperation(value = "Crea una nueva compañia")
    public void createCompany(@ApiParam(value = "Información de la compañia") @RequestBody CompanyDto company) {
        companyService.saveCompany(company);
    }

    @GetMapping(value = "internal/company")
    @ApiOperation(value = "Retorna todos las compañias registradas")
    public List<CompanyDetailProjection> getAllCompanies() {
        return companyService.getAllCompany();
    }

    @GetMapping(value = "internal/company/{id}")
    @ApiOperation(value = "Retorna una comapñia registrada en funcion del id")
    public CompanyDetailProjection getCompany(@PathVariable int id) {
        return companyService.getCompanyById(id);
    }

    @PutMapping(value = "internal/company/{id}")
    @ApiOperation(value = "Actualiza la información de una compañia")
    public void updateCompany(@PathVariable int id, @ApiParam(value = "Información de la compañia") @RequestBody CompanyDto company) {
        companyService.updateCompany(id, company);
    }

    @DeleteMapping(value = "internal/company/{id}")
    @ApiOperation(value = "Elimina una compañia")
    public void deleteCompany(@PathVariable int id) {
        companyService.deleteCompanyById(id);
    }
}
