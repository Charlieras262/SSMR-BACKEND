package com.g5.ssmr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "auditoria_resultado", schema = "g5_ssmr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_auditoria")
    private Integer auditoria;

    @Column(name = "id_requisitos")
    private Integer requisito;

    @Column(name = "id_resultado")
    private Integer idResultado;

    @Column(name = "respaldo")
    private String respaldo;
}
