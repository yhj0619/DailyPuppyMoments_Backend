package com.example.dpm.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Integer postId;
    private Long memberId; // Use memberId instead of MemberEntity to decouple
    private String title;
    private String content;
    private LocalDate postDate;
    private Integer imgId;
    // 태그 필드를 List<TagDto>로 설정
    private List<TagDto> tags;
    private List<CommentDto> comments;
    private String emoji;
    private Integer totalLikeHeart; // Total likes
    private boolean myLikeHeart; // User's like status
}
