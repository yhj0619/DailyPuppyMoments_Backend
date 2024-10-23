package com.example.dpm.post.service;

import java.util.List;

import com.example.dpm.post.dto.CommentDto;
import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;

public interface CommentService {
	public CommentDto get(Integer commentId); // 댓글 조회
	
	public Integer create(CommentDto commentDto); // 댓글 생성
	
	public void modify(CommentDto commentDto); // 댓글 수정
	
	public void remove(Integer postId, Integer commentId); // 댓글 삭제
	
	public List<CommentDto> getAllComments(Integer postId);// 게시글 당 댓글 리스트 조회

	PageResponseDto<CommentDto> getCommentsByPostId(Integer postId, PageRequestDto pageRequestDto);

	
}
