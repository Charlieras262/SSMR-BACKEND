package com.g5.ssmr.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateUserDto {
    private String name;
    private String lastName;
    private Integer state;
    private String email;
    private List<Integer> roles;
}
