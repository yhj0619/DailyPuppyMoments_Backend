package com.example.dpm.post.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;
import com.example.dpm.post.dto.PostDto;
import com.example.dpm.post.model.ImgEntity;
import com.example.dpm.post.service.ImgService;
import com.example.dpm.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@CrossOrigin("*")
public class PostController {
	private final PostService postService;

	
    private final ImgService imgService;


	private final String FOLDER_PATH = "c:\\images\\"; // 로컬 저장 경로

	// 사진 업로드 완료 버튼에 적용 시킬 api
	// 사진 url string으로 변환하는 controller
	@PostMapping("/img")
	public ResponseEntity<String> uploadImage(@RequestPart(value = "image") MultipartFile image) {
        try {
            ImgEntity imgEntity = imgService.uploadImage(image);
            return ResponseEntity.status(HttpStatus.CREATED).body(imgEntity.getImgId().toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Image upload failed: " + e.getMessage());
        }
    }

	// 게시물 생성
	@PostMapping("/")
	public ResponseEntity<Integer> createPost(@Validated @RequestBody PostDto postDto) {
		System.out.println("||||||||PostController_create_memID: " + postDto.getMemberId());
		Integer postId = postService.create(postDto);
		System.out.println("||||||||PostController_create_postId: " + postId);
		return ResponseEntity.ok(postId); // 생성된 게시물 ID 반환
	}

//	// 게시물 생성
//	@PostMapping("/")
//	public ResponseEntity<Integer> createPost(
//			@Validated @RequestPart("postDto") PostDto postDto,
//			@RequestPart(value = "image") MultipartFile image) throws IOException {
//		 // 이미지 파일을 저장하고 파일 경로를 DTO에 설정
//        if (image != null && !image.isEmpty()) {
//            String imagePath = saveImage(image);
//            postDto.setImg(imagePath); // DTO에 이미지 경로 저장
//        }
//        
//        System.out.println("[[PostController create flag1]]");
//        
//        // 서비스 계층에서 게시물 생성 처리
//        Integer postId = postService.create(postDto);
//        
//        System.out.println("[[PostController create flag2]]" + postId);
//        // 결과 반환
//        return new ResponseEntity<>(postId, HttpStatus.CREATED);
//	}

	// 이미지 저장 메서드
//    private String saveImage(MultipartFile image) throws IOException {
//        String originalFilename = image.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String uniqueFileName = UUID.randomUUID().toString() + extension;
//        String fullPath = FOLDER_PATH + uniqueFileName;
//        System.out.println("[[PostController saveImage flag3]]" + fullPath);
//        // 이미지 파일을 저장
//        File dest = new File(fullPath);
//        image.transferTo(dest);
//        System.out.println("[[PostController saveImage flag3]]" + fullPath);
//        return fullPath; // 파일 경로 반환
//    }

	// 게시물 조회
	@GetMapping("/{postId}")
	public ResponseEntity<PostDto> getPost(@PathVariable("postId") Integer postId) {
		PostDto postDto = postService.get(postId);
		System.out.println("||||||||PostController_get_postTitle: " + postDto.getTitle());
		return ResponseEntity.ok(postDto); // 게시물 데이터 반환
	}

	// 게시물 수정
	@PutMapping("/{postId}")
	public ResponseEntity<Void> modifyPost(@PathVariable("postId") Integer postId, @RequestBody PostDto postDto) {
		// 수정하려는 게시글의 ID를 DTO에 설정
		postDto.setPostId(postId);
		System.out.println("||||||||PostController_modify_postTitle: " + postDto.getTitle());
		// 게시물 수정
		postService.modify(postDto);
		System.out.println("||||||||PostController_modify_postTitle: " + postDto.getTitle());
		// 204 No Content 반환 (수정 성공)
		return ResponseEntity.noContent().build();
	}

	// 게시글 좋아요 기능
	@PostMapping("/{postId}/like")
	public ResponseEntity<String> toggleLike(@PathVariable("postId") Integer postId) {
		postService.toggleLike(postId);
		return new ResponseEntity<>("Like status updated.", HttpStatus.OK);
	}

	// 전체 게시글 조회 for test
//	@GetMapping("/list")
//	public ResponseEntity<List<PostDto>> getAllPosts() {
//		List<PostDto> posts = postService.getAllPosts();
//		return ResponseEntity.ok(posts); // 게시글 리스트 반환
//	}

	// 페이지네이션된 게시물 리스트 조회 for test
	@GetMapping("/list")
	public ResponseEntity<PageResponseDto<PostDto>> getList(@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> response = postService.getList(pageRequestDto);
		return ResponseEntity.ok(response);
	}

	// 제목으로 게시물 검색
	@GetMapping("/list/search")
	public ResponseEntity<PageResponseDto<PostDto>> searchPostsByTitle(
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> response = postService.searchPostsByTitle(keyword, pageRequestDto);
		return ResponseEntity.ok(response);
	}

	// sort 조건에 따라 게시글 조회
	@GetMapping("/list/{sort}")
	public ResponseEntity<PageResponseDto<PostDto>> getAllPostsBySort(@ModelAttribute PageRequestDto pageRequestDto,
			@PathVariable("sort") String sort) {
		PageResponseDto<PostDto> response;

		switch (sort) {
		case "latest":
			response = postService.getAllPostsOrderByDateLatest(pageRequestDto); // 최신순
			break;
		case "earliest":
			response = postService.getAllPostsOrderByDateEarliest(pageRequestDto); // 오래된순
			break;
		case "likes":
			response = postService.getAllPostsOrderByLikes(pageRequestDto); // 좋아요 많은 순
			break;
		default:
			return ResponseEntity.badRequest().build(); // 잘못된 정렬 기준
		}

		return ResponseEntity.ok(response);
	}

	// tag 검색 후 페이지네이션 게시글 조회
	@GetMapping("/list/search/tag/{tagName}")
	public ResponseEntity<PageResponseDto<PostDto>> searchPostsByTag(@PathVariable("tagName") String tagName,
			@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> response = postService.searchPostsByTag(tagName, pageRequestDto);
		return ResponseEntity.ok(response);
	}

	// 특정 회원의 게시글 조회
	@GetMapping("/list/mypost/{memberId}") // URL 경로에서 memberId를 받음
	public ResponseEntity<PageResponseDto<PostDto>> getAllMyPosts(@PathVariable("memberId") Long memberId,
			@ModelAttribute PageRequestDto pageRequestDto) {
		PageResponseDto<PostDto> myPosts = postService.getAllMyPosts(memberId, pageRequestDto); // 특정 회원의 게시글 조회
		System.out.println("#####[PostController] myposts: " + myPosts.getDtoList());
		return ResponseEntity.ok(myPosts); // 게시글 리스트 반환
	}

	// 게시글 삭제
	@DeleteMapping("/{postId}") // URL 경로에서 postId를 받음
	public ResponseEntity<Void> deletePost(@PathVariable("postId") Integer postId) {
		postService.remove(postId); // 게시글 삭제 서비스 호출
		return ResponseEntity.noContent().build(); // 204 No Content 반환
	}
}
