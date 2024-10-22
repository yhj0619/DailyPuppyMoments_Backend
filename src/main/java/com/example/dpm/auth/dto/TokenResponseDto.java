package com.example.dpm.auth.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}