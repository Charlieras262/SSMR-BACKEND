package com.g5.ssmr.dto;

import lombok.Data;

import java.util.List;

@Data
public class EditUserDto {
    private Integer state;
    private List<Integer> roles;
}
