package com.g5.ssmr.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.g5.ssmr.dto.CreateUserDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "g5_ssmr")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id_user", length = 20)
    private String idUser;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "last_name", length = 100)
    private String lastName;
    @Column(name = "email", length = 50)
    private String email;
    @JsonIgnore
    @Column(name = "password")
    private String password;
    @Column(name = "state")
    private Integer state;
    @JsonIgnore
    @Column(name = "token")
    private String token;

    public User(String name, String lastName, Integer state) {
        this.name = name;
        this.lastName = lastName;
        this.state = state;
    }

    public static User fromDto(CreateUserDto dto) {
        return new User(dto.getName(), dto.getLastName(), dto.getState());
    }
}
