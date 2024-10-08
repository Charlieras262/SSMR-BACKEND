package com.g5.ssmr.projections;

public interface AuditDetailProjection {
    Integer getId();
    String getEmpresa();
    String getMarcoRegulatorio();
    String getEstado();
    String getObservaciones();
    String getNombreAuditor();
    String getRepresentante();
}
