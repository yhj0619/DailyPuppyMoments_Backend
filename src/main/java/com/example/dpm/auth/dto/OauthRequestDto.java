package com.example.dpm.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OauthRequestDto {
    private String accessToken;
    private String refreshToken;
    
    private String code;
}
