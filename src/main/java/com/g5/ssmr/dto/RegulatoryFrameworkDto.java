package com.g5.ssmr.dto;

import com.g5.ssmr.models.Requirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegulatoryFrameworkDto {

    private int id;
    private String nombre;
    private String descripcion;
    private String documento;
    private List<RequirementDto> requisitos;
}
