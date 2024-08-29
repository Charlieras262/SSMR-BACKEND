package com.g5.ssmr.services;

import com.g5.ssmr.dto.RegulatoryFrameworkDto;
import com.g5.ssmr.dto.RequirementDto;
import com.g5.ssmr.models.RegulatoryFramework;
import com.g5.ssmr.models.Requirement;
import com.g5.ssmr.repositories.RegulatoryFrameworkRepository;
import com.g5.ssmr.repositories.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegulatoryFrameworkService {

    @Autowired
    RegulatoryFrameworkRepository regulatoryFrameworkRepository;

    @Autowired
    RequirementRepository requirementRepository;

    public void createRegulatoryFramework(RegulatoryFrameworkDto dto) {
        final RegulatoryFramework regulatoryFramework = regulatoryFrameworkRepository.save(RegulatoryFramework.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .docRespaldo(dto.getDocumento())
                .build()
        );

        dto.getRequisitos().forEach(requirement -> {
            requirementRepository.save(Requirement.builder()
                    .nombre(requirement.getNombre())
                    .descripcion(requirement.getDescripcion())
                    .marcoRegulatorio(regulatoryFramework.getId())
                    .build()
            );
        });
    }

    public List<RegulatoryFrameworkDto> getAllRegulatoryFrameworks() {
        return regulatoryFrameworkRepository.findAll().stream().map(regulatoryFramework -> RegulatoryFrameworkDto.builder()
                .id(regulatoryFramework.getId())
                .nombre(regulatoryFramework.getNombre())
                .descripcion(regulatoryFramework.getDescripcion())
                .documento(regulatoryFramework.getDocRespaldo())
                .requisitos(requirementRepository.findByMarcoRegulatorio(regulatoryFramework.getId()).stream().map(requirement -> RequirementDto.builder()
                                .nombre(requirement.getNombre())
                                .descripcion(requirement.getDescripcion())
                                .build()
                        ).collect(Collectors.toList())
                ).build()
        ).collect(Collectors.toList());
    }

    public RegulatoryFrameworkDto getRegulatoryFrameworkById(Integer id) {
        final RegulatoryFramework regulatoryFramework = regulatoryFrameworkRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id del marco regulatorio.")
        );

        return RegulatoryFrameworkDto.builder()
                .id(regulatoryFramework.getId())
                .nombre(regulatoryFramework.getNombre())
                .descripcion(regulatoryFramework.getDescripcion())
                .documento(regulatoryFramework.getDocRespaldo())
                .requisitos(requirementRepository.findByMarcoRegulatorio(id).stream().map(requirement -> RequirementDto.builder()
                                .id(requirement.getId())
                                .nombre(requirement.getNombre())
                                .descripcion(requirement.getDescripcion())
                                .build()
                        ).collect(Collectors.toList())
                ).build();
    }

    public void updateRegulatoryFramework(Integer id, RegulatoryFrameworkDto dto) {
        final RegulatoryFramework regulatoryFramework = regulatoryFrameworkRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id del marco regulatorio.")
        );

        regulatoryFramework.setNombre(dto.getNombre());
        regulatoryFramework.setDescripcion(dto.getDescripcion());
        regulatoryFramework.setDocRespaldo(dto.getDocumento());
        regulatoryFrameworkRepository.save(regulatoryFramework);

        requirementRepository.deleteByMarcoRegulatorio(id);
        dto.getRequisitos().forEach(requirement -> {
            requirementRepository.save(Requirement.builder()
                    .nombre(requirement.getNombre())
                    .descripcion(requirement.getDescripcion())
                    .marcoRegulatorio(regulatoryFramework.getId())
                    .build()
            );
        });
    }

    public void deleteRegulatoryFrameworkById(Integer id) {
        regulatoryFrameworkRepository.deleteById(id);
        requirementRepository.deleteByMarcoRegulatorio(id);
    }
}
