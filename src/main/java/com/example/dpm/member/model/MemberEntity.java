package com.example.dpm.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
public class MemberEntity {
	@Id
    private Long member_id; // 사용자 카카오 고유 아이디

    private String socialId; // 사용자 카카오 이메일

    private String nickname; //사용자 카카오 이름
    
    private String profile_image; //사용자 카카오 프로필 사진
    
    
    private int point; //사용자 획득 점수
    
    
    private boolean attendance; //사용자 출석 여부
    
    
    private boolean is_deleted; //사용자 탈퇴 여부

    
    @Column(name = "refresh_token")
    private String refreshToken; //카카오 사용자 refresh token
}
