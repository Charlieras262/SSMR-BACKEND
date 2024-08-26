package com.g5.ssmr.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "catalogue_child", schema = "g5_ssmr")
public class CatalogueChild implements Serializable {

    private int idCatalogueChild;
    private int idCatalogue;
    private String name;
    private String description;

    public CatalogueChild() {
    }

    public CatalogueChild(int idCatalogueChild, int idCatalogue, String name, String description) {
        this.idCatalogueChild = idCatalogueChild;
        this.idCatalogue = idCatalogue;
        this.name = name;
        this.description = description;
    }

    @Id
    @Column(name = "id_catalogue_child", unique = true, nullable = false)
    public int getIdCatalogueChild() {
        return idCatalogueChild;
    }

    public void setIdCatalogueChild(int idCatalogueChild) {
        this.idCatalogueChild = idCatalogueChild;
    }

    @Column(name = "id_catalogue")
    public int getIdCatalogue() {
        return idCatalogue;
    }

    public void setIdCatalogue(int idCatalogue) {
        this.idCatalogue = idCatalogue;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
