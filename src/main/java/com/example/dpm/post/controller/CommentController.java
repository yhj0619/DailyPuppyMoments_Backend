package com.example.dpm.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dpm.post.dto.CommentDto;
import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;
import com.example.dpm.post.dto.PostDto;
import com.example.dpm.post.service.CommentService;
import com.example.dpm.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class CommentController {

	private final CommentService commentService;
	
	private final PostService postService;

	// 댓글 생성
	@PostMapping("/{postId}/comment")
	public ResponseEntity<Integer> createComment(@Validated @RequestBody CommentDto commentDto, 
							@PathVariable("postId") Integer postId) {
		System.out.println("||||||||CommentController_create_postID: " + commentDto.getPostId());
		Integer commentId = commentService.create(commentDto);
		System.out.println("||||||||PostController_create_commentId: " + commentId + ",  postId : " + postId);
		return ResponseEntity.ok(commentId); // 생성된 게시물 ID 반환
	}
	
	// 댓글 리스트 조회
	@GetMapping("/{postId}/comment")
	public ResponseEntity<PageResponseDto<CommentDto>> getPostComments(
	        @PathVariable("postId") Integer postId,
	        @ModelAttribute PageRequestDto pageRequestDto) { // PageRequestDto 추가

	    PostDto postDto = postService.get(postId);
	    // 댓글을 페이지네이션하여 가져오기
	    PageResponseDto<CommentDto> commentsResponse = commentService.getCommentsByPostId(postId, pageRequestDto);

	    System.out.println("||||||||PostController_get_postTitle: " + postDto.getTitle());
	    return ResponseEntity.ok(commentsResponse); // 댓글 데이터 반환
	}
	
	// 댓글 삭제
	@DeleteMapping("/{postId}/comment/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable("postId") Integer postId, @PathVariable("commentId") Integer commentId) {
	    commentService.remove(postId, commentId); // 댓글 삭제 서비스 호출
	    return ResponseEntity.noContent().build(); // 204 No Content 반환
	}
}
