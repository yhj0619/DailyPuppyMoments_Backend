package com.example.dpm.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private int postId;
    private Long memberId; // Use memberId instead of MemberEntity to decouple
    private String title;
    private String content;
    private LocalDate postDate;
    private String img;
    private int tagId;
    private String emoji;
    private int totalLikeHeart; // Total likes
    private boolean myLikeHeart; // User's like status
}
