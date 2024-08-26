package com.g5.ssmr.models;

import com.g5.ssmr.dto.CreateUserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "g5_ssmr")
public class User {
    private String idUser;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Integer state;
    private String token;

    public User() {
    }

    public User(String name, String lastName, Integer state) {
        this.name = name;
        this.lastName = lastName;
        this.state = state;
    }

    public static User fromDto(CreateUserDto dto) {
        return new User(dto.getName(), dto.getLastName(), dto.getState());
    }

    @Id
    @Column(name = "id_user", length = 20)
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "last_name", length = 100)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email", length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Column(name = "password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @JsonIgnore
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
