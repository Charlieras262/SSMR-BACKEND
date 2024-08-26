package com.g5.ssmr.dto;

import com.g5.ssmr.projections.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileDto {
    private String username;
    private List<UserRole> roles;
    private String name;
    private String lastName;
    private String email;
    private Integer state;
}
