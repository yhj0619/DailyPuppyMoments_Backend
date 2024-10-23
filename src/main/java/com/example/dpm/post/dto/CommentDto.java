package com.example.dpm.post.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private int commentId;         // 댓글 ID
    private Long memberId;         // 회원 ID
    private int postId;            // 게시물 ID
    private String content;        // 댓글 내용
    private LocalDate commentDate; // 댓글 작성 날짜
}
