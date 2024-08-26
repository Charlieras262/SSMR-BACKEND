package com.g5.ssmr.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePassDto {
    private String oldPass;
    private String newPass;
    private String username;
}
