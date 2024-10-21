package com.example.dpm.member.model;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    private Long member_id;

	@Nullable
    private String socialId;

    private String nickname;
    
    private String profile_image;
//    
//    private int point;
//    
//    private boolean attendance;
//    
//    private boolean is_deleted;

    @Nullable
    @Column(name = "refresh_token")
    private String refreshToken;

}
