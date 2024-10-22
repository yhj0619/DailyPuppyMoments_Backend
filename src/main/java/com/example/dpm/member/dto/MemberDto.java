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
    
    private String nickname;

    private String profile_image;
    
    private int point; //사용자 획득 점수
    
    private boolean attendance; //사용자 출석 여부
    
    private boolean is_deleted; //사용자 탈퇴 여부

    private String refreshToken;
}
