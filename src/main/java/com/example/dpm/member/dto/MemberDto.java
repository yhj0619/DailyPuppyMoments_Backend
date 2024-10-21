package com.example.dpm.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberDto {
    private Long member_id;

    private String socialId; //사용자 이메일

    private String profile_nickname;
    
    private String profile_image;

    private String refreshToken;
}
