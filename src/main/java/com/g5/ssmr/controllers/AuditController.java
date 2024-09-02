package com.g5.ssmr.controllers;

import com.g5.ssmr.dto.AuditDetailDto;
import com.g5.ssmr.dto.AuditDto;
import com.g5.ssmr.dto.CompanyDto;
import com.g5.ssmr.dto.ResultDto;
import com.g5.ssmr.models.Company;
import com.g5.ssmr.projections.AuditDetailProjection;
import com.g5.ssmr.services.AuditService;
import com.g5.ssmr.services.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class AuditController {

    @Autowired
    AuditService auditService;

    @PostMapping("internal/audit")
    @ApiOperation(value = "Crea una nueva auditoria")
    public void createAudit(@ApiParam(value = "Información de la auditoria") @RequestBody AuditDto audit) {
        auditService.createAudit(audit);
    }

    @GetMapping("internal/audit/username")
    @ApiOperation(value = "Retorna todas las auditorias registradas")
    public List<AuditDetailProjection> getAllAuditsUser(@RequestHeader HttpHeaders headers) {
        return auditService.getAllAuditsUser(headers);
    }

    @GetMapping("internal/audit/company")
    @ApiOperation(value = "Retorna todas las auditorias registradas")
    public List<AuditDetailProjection> getAllAudits(@RequestHeader HttpHeaders headers) {
        return auditService.getAllAudits(headers);
    }

    @GetMapping("internal/audit/{id}")
    @ApiOperation(value = "Retorna una auditoria registrada en función del id")
    public AuditDetailDto getAudit(@PathVariable int id) {
        return auditService.getAuditById(id);
    }

    @PutMapping("internal/audit/save/{id}")
    @ApiOperation(value = "Guarda el estado actual de una auditoria, util para almacenar el progreso sin finalizar la auditoria")
    public void startAudit(@PathVariable int id, @RequestBody List<ResultDto> dto) {
        auditService.startContinueAudit(id, dto);
    }

    @PutMapping("internal/audit/finish/{id}")
    @ApiOperation(value = "Finaliza una auditoria")
    public void finishAudit(@PathVariable int id, @RequestBody String comment) {
        auditService.finishAudit(id, comment);
    }
}
