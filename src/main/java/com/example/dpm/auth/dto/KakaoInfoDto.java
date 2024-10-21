package com.example.dpm.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    private Long id;
    private String email;
    private String nickname;  // 추가된 필드
    private String profileImage;  // 추가된 필드
    

    public KakaoInfoDto(Map<String, Object> attributes) {
        this.id = Long.valueOf(attributes.get("id").toString());
        this.email = attributes.get("kakao_account") != null 
            ? ((Map<String, Object>) attributes.get("kakao_account")).get("email").toString() 
            : "";
        
        this.nickname = ((Map<String, Object>) attributes.get("properties")).get("nickname").toString();
        this.profileImage = ((Map<String, Object>) attributes.get("properties")).get("profile_image").toString();
    }
}
