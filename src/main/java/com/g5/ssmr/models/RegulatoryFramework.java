package com.g5.ssmr.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "marcos_regulatorios", schema = "g5_ssmr")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegulatoryFramework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "doc_respaldo")
    private String docRespaldo;
}
