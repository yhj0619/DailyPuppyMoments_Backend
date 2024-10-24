package com.example.dpm.post.model;

import java.time.LocalDate;
import java.util.List;

import com.example.dpm.member.model.MemberEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post_image")
public class ImgEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imgId;

    private String filePath; // 이미지 파일 경로
    
//    // PostEntity와의 관계 (단방향)
    @OneToOne(mappedBy = "img")
    private PostEntity post;
}
