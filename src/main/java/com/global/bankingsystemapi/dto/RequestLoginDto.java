package com.global.bankingsystemapi.dto;

import lombok.Data;

@Data
public class RequestLoginDto {
    private String username;
    private String password;
}
