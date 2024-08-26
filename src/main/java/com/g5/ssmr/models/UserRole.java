package com.g5.ssmr.models;

import javax.persistence.*;

@Entity
@Table(name = "user_roles", schema = "g5_ssmr")
@IdClass(UserRoleId.class)
public class UserRole {
    private Integer idRole;
    private String idUser;

    public UserRole() {
    }

    public UserRole(Integer idRole, String idUser) {
        this.idRole = idRole;
        this.idUser = idUser;
    }

    @Id
    @Column(name = "id_role")
    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    @Id
    @Column(name = "id_user", length = 20)
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
