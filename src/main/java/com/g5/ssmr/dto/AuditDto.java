package com.g5.ssmr.dto;

import lombok.Data;

@Data
public class AuditDto {
    private Integer empresa;
    private Integer marcoRegulatorio;
    private String auditor;
}
