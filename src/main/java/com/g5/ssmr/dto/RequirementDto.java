package com.g5.ssmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequirementDto {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer estado;
    private String respaldo;
}
