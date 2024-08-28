package com.g5.ssmr.dto;

import com.g5.ssmr.projections.AuditDetailProjection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuditDetailDto {
    private AuditDetailProjection auditoriaDetalle;
    private RegulatoryFrameworkDto marcoRegulatorio;
}
