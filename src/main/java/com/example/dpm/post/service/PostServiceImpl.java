package com.example.dpm.post.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.post.dto.CommentDto;
import com.example.dpm.post.dto.PageRequestDto;
import com.example.dpm.post.dto.PageResponseDto;
import com.example.dpm.post.dto.PostDto;
import com.example.dpm.post.dto.TagDto;
import com.example.dpm.post.model.CommentEntity;
import com.example.dpm.post.model.PostEntity;
import com.example.dpm.post.model.TagEntity;
import com.example.dpm.post.repository.PostRepository;
import com.example.dpm.post.repository.TagRepository;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TagRepository tagRepository;

	@Override
	public PostDto get(Integer postId) {
		Optional<PostEntity> result = postRepository.findById(postId);
		PostEntity post = result.orElseThrow();
		return toDto(post);
	}
	
	@Override
    public void toggleLike(Integer postId) {
		Optional<PostEntity> result = postRepository.findById(postId);
		PostEntity post = result.orElseThrow();

		post.toggleLikeHeart();
		
        postRepository.save(post);
    }

	@Override
	public Integer create(PostDto dto) {
	    Optional<MemberEntity> member = memberRepository.findById(dto.getMemberId());
	    MemberEntity foundMember = member.orElseThrow();
	    System.out.println("[[PostService]] foundMember: " + foundMember.toString());
	    
	    // DTO에서 태그 DTO 리스트를 가져와서 TagEntity들을 찾거나 새로 생성
	    List<TagEntity> tags = new ArrayList<>();
	    for (TagDto tagDto : dto.getTags()) {
	        Optional<TagEntity> existingTag = tagRepository.findByTagName(tagDto.getName());
	        TagEntity tagEntity;
	        if (existingTag.isPresent()) {
	            // 기존 태그를 사용
	            tagEntity = existingTag.get();
	        } else {
	            // 새로운 태그 생성
	            tagEntity = new TagEntity();
	            tagEntity.setTagName(tagDto.getName());
	            tagEntity = tagRepository.save(tagEntity); // 새로 생성된 태그 저장
	            System.out.println("[[PostService]] tag 생성완료!");
	        }
	        tags.add(tagEntity);
	        System.out.println("[[PostService]] tag 리스트: " + tags.toString());
	    }
	    
	    // PostEntity 생성
	    PostEntity postEntity = toEntity(dto, foundMember, tags, null);
	    System.out.println("[[PostService]] postEntity: " + postEntity.toString());
	    
	    PostEntity result = postRepository.save(postEntity);
	    System.out.println("[[PostService]] postEntity SAVE DB ");
	    return result.getPostId(); // 몇번째인지 return
	}



	@Override
	public void modify(PostDto dto) {
	    // 수정할 게시글을 찾는다.
	    PostEntity postEntity = postRepository.findById(dto.getPostId())
	        .orElseThrow(() -> new RuntimeException("Post not found"));

	    // 제목, 내용, 이미지, 이모지 수정
	    postEntity.setTitle(dto.getTitle());
	    postEntity.setContent(dto.getContent());
	    postEntity.setImg(dto.getImg());
	    postEntity.setEmoji(dto.getEmoji());
	    
	    // 태그 업데이트
	    List<TagEntity> tags = new ArrayList<>();
	    for (TagDto tagDto : dto.getTags()) {
	        Optional<TagEntity> existingTag = tagRepository.findByTagName(tagDto.getName());
	        TagEntity tagEntity;
	        if (existingTag.isPresent()) {
	            tagEntity = existingTag.get();
	        } else {
	            tagEntity = new TagEntity();
	            tagEntity.setTagName(tagDto.getName());
	            tagRepository.save(tagEntity);
	        }
	        tags.add(tagEntity);
	    }

	    // 게시물의 태그 업데이트
	    postEntity.setTags(tags);

	    // 수정된 게시글을 저장
	    postRepository.save(postEntity);
	}


	@Override
	public void remove(Integer postId) {
		 // 게시글이 존재하는지 확인
        Optional<PostEntity> postEntity = postRepository.findById(postId);
        if (postEntity.isPresent()) {
            postRepository.deleteById(postId); // 게시글 삭제
            System.out.println("[[PostService]] 게시글 삭제 완료: " + postId);
        } else {
            throw new RuntimeException("게시글이 존재하지 않습니다."); // 게시글이 없을 경우 예외 처리
        }
	}
	
	@Override
	public List<PostDto> getAllPosts() {
		return postRepository.findAll().stream()
	            .map(this::toDto) // PostEntity를 PostDto로 변환하는 메서드
	            .collect(Collectors.toList());
	}
	
	@Override
	public PageResponseDto<PostDto> getList(PageRequestDto pageRequestDto) {
	    // 페이지네이션을 위한 계산
	    int page = pageRequestDto.getPage() - 1;  // JPA는 페이지 번호가 0부터 시작
	    int size = pageRequestDto.getSize();
	    int offset = page * size;

	    // 페이지네이션에 맞춰 게시물 목록 조회
	    List<PostEntity> postEntities = postRepository.findPostsWithPagination(offset, size);
	    
	    // 게시물 총 개수 조회
	    long totalPosts = postRepository.count();
	    
	    // PostEntity를 PostDto로 변환
	    List<PostDto> postDtos = postEntities.stream()
	            .map(this::toDto)
	            .collect(Collectors.toList());

	    // PageResponseDto로 변환하여 반환
	    return PageResponseDto.<PostDto>withAll()
	            .dtoList(postDtos)
	            .pageRequestDto(pageRequestDto)
	            .total(totalPosts)
	            .build();
	}
	
	@Override
	public PageResponseDto<PostDto> searchPostsByTitle(String keyword, PageRequestDto pageRequestDto) {
	    PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
	    Page<PostEntity> postEntities = postRepository.searchByTitle(keyword, pageRequest);

	    return new PageResponseDto<>(
	        postEntities.stream().map(this::toDto).collect(Collectors.toList()),
	        pageRequestDto,
	        postEntities.getTotalElements()
	    );
	}

	@Override
	public PageResponseDto<PostDto> getAllPostsOrderByDateLatest(PageRequestDto pageRequestDto) {
	    PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
	    Page<PostEntity> postEntities = postRepository.findAllOrderByPostDateLatest(pageRequest);

	    return new PageResponseDto<>(
	        postEntities.stream().map(this::toDto).collect(Collectors.toList()),
	        pageRequestDto,
	        postEntities.getTotalElements()
	    );
	}
	
	@Override
	public PageResponseDto<PostDto> getAllPostsOrderByDateEarliest(PageRequestDto pageRequestDto) {
	    PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
	    Page<PostEntity> postEntities = postRepository.findAllOrderByPostDateEarliest(pageRequest);

	    return new PageResponseDto<>(
	        postEntities.stream().map(this::toDto).collect(Collectors.toList()),
	        pageRequestDto,
	        postEntities.getTotalElements()
	    );
	}
	
	@Override
	public PageResponseDto<PostDto> getAllPostsOrderByLikes(PageRequestDto pageRequestDto) {
	    PageRequest pageRequest = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
	    Page<PostEntity> postEntities = postRepository.findAllOrderByLikes(pageRequest); // 좋아요 많은 순으로 조회

	    return new PageResponseDto<>(
	        postEntities.stream().map(this::toDto).collect(Collectors.toList()),
	        pageRequestDto,
	        postEntities.getTotalElements()
	    );
	}
	@Override
	public PageResponseDto<PostDto> searchPostsByTag(String tagName, PageRequestDto pageRequestDto) {
	    // 페이지네이션을 위한 계산
	    int page = pageRequestDto.getPage() - 1; // JPA는 페이지 번호가 0부터 시작
	    int size = pageRequestDto.getSize();
	    
	    Page<PostEntity> postEntities = postRepository.findPostsByTagName(tagName, PageRequest.of(page, size));
	    
	    // PostEntity를 PostDto로 변환
	    List<PostDto> postDtos = postEntities.getContent().stream()
	            .map(this::toDto)
	            .collect(Collectors.toList());

	    // PageResponseDto로 변환하여 반환
	    return PageResponseDto.<PostDto>withAll()
	            .dtoList(postDtos)
	            .pageRequestDto(pageRequestDto)
	            .total(postEntities.getTotalElements())
	            .build();
	}

	@Override
	public PageResponseDto<PostDto> getAllMyPosts(Long memberId, PageRequestDto pageRequestDto) {
		// 페이지네이션을 위한 계산
	    int page = pageRequestDto.getPage() - 1; // JPA는 페이지 번호가 0부터 시작
	    int size = pageRequestDto.getSize();
	    
	    Page<PostEntity> postEntities = postRepository.findByMember_MemberId(memberId,PageRequest.of(page, size));
	    System.out.println("#####[postService] postEntities done" + postEntities.toString());
	    List<PostDto> postDtos = postEntities.getContent().stream()
	    		.map(this::toDto)
	    		.collect(Collectors.toList());
	    System.out.println("#####[postService] postDtos done" + postDtos.toString());
	    return PageResponseDto.<PostDto>withAll()
	    		.dtoList(postDtos)
	    		.pageRequestDto(pageRequestDto)
	            .total(postEntities.getTotalElements())
	            .build();
	}

	// PostDto -> PostEntity 변환
    public PostEntity toEntity(PostDto dto, MemberEntity member, List<TagEntity> tags, List<CommentEntity> comments) {
        return PostEntity.builder()
            .postId(dto.getPostId())
            .member(member)
            .title(dto.getTitle())
            .content(dto.getContent())
            .postDate(dto.getPostDate())
            .img(dto.getImg())
            .emoji(dto.getEmoji())
            .totalLikeHeart(dto.getTotalLikeHeart())
            .myLikeHeart(dto.isMyLikeHeart())
            .tags(tags)  // 여러 태그 설정
            .comments(comments)
            .build();
    }

    // PostEntity -> PostDto 변환
    public PostDto toDto(PostEntity entity) {
        List<TagDto> tags = entity.getTags().stream()
            .map(tag -> new TagDto(tag.getTagId(), tag.getTagName()))
            .collect(Collectors.toList());
        
        List<CommentDto> comments = entity.getComments().stream()
        		.map(comment -> new CommentDto(comment.getCommentId(), comment.getMember().getMember_id(), comment.getPost().getPostId(), comment.getContent(), comment.getCommentDate()))
        		.collect(Collectors.toList());
        return PostDto.builder()
            .postId(entity.getPostId())
            .memberId(entity.getMember().getMember_id())
            .title(entity.getTitle())
            .content(entity.getContent())
            .postDate(entity.getPostDate())
            .img(entity.getImg())
            .emoji(entity.getEmoji())
            .totalLikeHeart(entity.getTotalLikeHeart())
            .myLikeHeart(entity.isMyLikeHeart())
            .tags(tags)
            .comments(comments)
            .build();
    }
}
