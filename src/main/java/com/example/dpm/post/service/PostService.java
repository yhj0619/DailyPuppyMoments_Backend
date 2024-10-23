package com.example.dpm.post.service;

import java.util.List;

import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;
import com.example.dpm.post.dto.PostDto;

public interface PostService {
	
	public PostDto get(Integer postId); // 게시물 조회
	
	public Integer create(PostDto dto); // 게시물 생성
	
	public void modify(PostDto dto); // 게시물 수정
	
	public void remove(Integer postId); // 게시물 삭제
	
	public PageResponseDto<PostDto> getList(PageRequestDto pageRequestDto);
	
	public List<PostDto> getAllPosts();// 게시물 리스트 조회
	
	//public List<PostDto> getAllMyPosts(Long memberId); // 본인 게시물 조회

	PageResponseDto<PostDto> searchPostsByTitle(String keyword, PageRequestDto pageRequestDto);
	
    PageResponseDto<PostDto> getAllPostsOrderByDateLatest(PageRequestDto pageRequestDto);
    
    PageResponseDto<PostDto> getAllPostsOrderByDateEarliest(PageRequestDto pageRequestDto);
    
    PageResponseDto<PostDto> getAllPostsOrderByLikes(PageRequestDto pageRequestDto);

	PageResponseDto<PostDto> searchPostsByTag(String tagName, PageRequestDto pageRequestDto);
}


