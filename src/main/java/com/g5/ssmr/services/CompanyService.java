package com.g5.ssmr.services;

import com.g5.ssmr.dto.CompanyDto;
import com.g5.ssmr.models.Company;
import com.g5.ssmr.projections.CompanyDetailProjection;
import com.g5.ssmr.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    public void saveCompany(CompanyDto company) {
        companyRepository.save(Company.builder()
                .nombre(company.getNombre())
                .descripcion(company.getDescripcion())
                .representante(company.getRepresentante())
                .build()
        );
    }

    public List<CompanyDetailProjection> getAllCompany() {
        return companyRepository.getAll();
    }

    public CompanyDetailProjection getCompanyById(int id) {
        return companyRepository.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id de la empresa."));
    }

    public void updateCompany(int id, CompanyDto dto) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id de la empresa."));

        company.setNombre(dto.getNombre());
        company.setDescripcion(dto.getDescripcion());
        company.setRepresentante(dto.getRepresentante());

        companyRepository.save(company);
    }

    public void deleteCompanyById(int id) {
        companyRepository.deleteById(id);
    }
}
