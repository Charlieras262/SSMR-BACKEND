package com.g5.ssmr.services;

import com.g5.ssmr.config.security.JwtUtil;
import com.g5.ssmr.dto.AuditDetailDto;
import com.g5.ssmr.dto.AuditDto;
import com.g5.ssmr.dto.RegulatoryFrameworkDto;
import com.g5.ssmr.dto.ResultDto;
import com.g5.ssmr.models.Audit;
import com.g5.ssmr.models.AuditResult;
import com.g5.ssmr.projections.AuditDetailProjection;
import com.g5.ssmr.repositories.AuditRepository;
import com.g5.ssmr.repositories.AuditResultRepository;
import com.g5.ssmr.utils.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    AuditRepository auditRepository;

    @Autowired
    AuditResultRepository resulRepository;

    @Autowired
    RegulatoryFrameworkService regulatoryFrameworkService;

    public void createAudit(AuditDto dto) {
        auditRepository.save(Audit.builder()
                .empresa(dto.getEmpresa())
                .marcoRegulatorio(dto.getMarcoRegulatorio())
                .idEstado(Catalog.AuditStatus.CREATED)
                .auditor(dto.getAuditor())
                .build()
        );
    }

    public List<AuditDetailProjection> getAllAuditsUser(HttpHeaders headers) {
        final String username = JwtUtil.parseToken(headers.getFirst("Authorization"));
        return auditRepository.getAllUser(username);
    }

    public List<AuditDetailProjection> getAllAudits(HttpHeaders headers) {
        final String username = JwtUtil.parseToken(headers.getFirst("Authorization"));
        return auditRepository.getAll(username);
    }

    public List<AuditDetailProjection> getAllAuditsAdmin() {
        return auditRepository.getAllAdmin();
    }

    public AuditDetailDto getAuditById(Integer id) {
        final Audit audit = auditRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id de la auditoria.")
        );

        final AuditDetailProjection auditDetail = auditRepository.getById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id de la auditoria.")
        );

        final RegulatoryFrameworkDto rf = regulatoryFrameworkService.getRegulatoryFrameworkById(audit.getMarcoRegulatorio());

        rf.getRequisitos().forEach(requirement -> requirement.setEstado(resulRepository.findByAuditoriaAndRequisito(id, requirement.getId())));
        rf.getRequisitos().forEach(requirement -> requirement.setRespaldo(resulRepository.findByAuditoriaAndRequisitoRespaldo(id, requirement.getId())));

        rf.getRequisitos().stream().sorted((r1, r2) -> r1.getId().compareTo(r2.getId()));
        return AuditDetailDto.builder()
                .auditoriaDetalle(auditDetail)
                .marcoRegulatorio(rf)
                .build();
    }

    public void startContinueAudit(Integer id, List<ResultDto> results) {
        final Audit audit = auditRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id de la auditoria.")
        );

        if (!Arrays.asList(Catalog.AuditStatus.CREATED, Catalog.AuditStatus.IN_PROCESS).contains(audit.getIdEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La auditoria no se encuentra en estado creado o en progreso.");
        }

        audit.setIdEstado(Catalog.AuditStatus.IN_PROCESS);
        auditRepository.save(audit);

        results.forEach(result -> {
            Integer idAudit = resulRepository.findByAuditoriaAndRequisito2(id, result.getRequisito());
            System.out.println("Id de la auditoria" + idAudit);
            resulRepository.save(AuditResult.builder()
                    .id(idAudit)
                    .auditoria(id)
                    .requisito(result.getRequisito())
                    .idResultado(result.getResultado())
                    .respaldo(result.getRespaldo())
                    .build()
            );
        });
    }

    public void finishAudit(Integer id, String comment) {
        final Audit audit = auditRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontro el id de la auditoria.")
        );

        if (audit.getIdEstado() != Catalog.AuditStatus.IN_PROCESS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La auditoria no se encuentra en estado en progreso.");
        }

        final RegulatoryFrameworkDto rf = regulatoryFrameworkService.getRegulatoryFrameworkById(audit.getMarcoRegulatorio());

        rf.getRequisitos().forEach(requirement -> requirement.setEstado(resulRepository.findByAuditoriaAndRequisito(id, requirement.getId())));

        if (rf.getRequisitos().stream().anyMatch(requirement -> requirement.getEstado() == null  && requirement.getLectura() == false)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se han registrado todos los resultados de los requisitos.");
        }

        audit.setIdEstado(Catalog.AuditStatus.FINISHED);
        audit.setObservaciones(comment);
        auditRepository.save(audit);
    }
}
