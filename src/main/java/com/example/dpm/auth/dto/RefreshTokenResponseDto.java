package com.example.dpm.auth.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}