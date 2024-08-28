package com.g5.ssmr.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_roles", schema = "g5_ssmr")
@IdClass(UserRoleId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @Id
    @Column(name = "id_role")
    private Integer idRole;
    @Id
    @Column(name = "id_user", length = 20)
    private String idUser;
}
