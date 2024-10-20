package com.example.dpm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberDto {
    private int member_id;

    private String socialId; //사용자 이메일

    private String nickname;

    private String refreshToken;
}
