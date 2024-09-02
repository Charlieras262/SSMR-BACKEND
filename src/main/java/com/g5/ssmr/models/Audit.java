package com.g5.ssmr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "auditoria", schema = "g5_ssmr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_empresa")
    private Integer empresa;

    @Column(name = "id_marco_regulatorio")
    private Integer marcoRegulatorio;

    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "observaciones", length = 300)
    private String observaciones;

    @Column(name = "auditor")
    private String auditor;
}
