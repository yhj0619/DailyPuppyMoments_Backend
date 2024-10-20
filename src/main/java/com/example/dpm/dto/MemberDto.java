package com.example.dpm.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MemberDto {
    private int member_id;

    private String socialId;

    private String nickname;
    
    private int point;
    
    private boolean attendance;
    
    private boolean is_deleted;

    private String refreshToken;
}
