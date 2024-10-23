package com.example.dpm.post.service;

import java.util.List;
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
import com.example.dpm.post.model.CommentEntity;
import com.example.dpm.post.model.PostEntity;
import com.example.dpm.post.repository.CommentRepository;
import com.example.dpm.post.repository.PostRepository;

@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	
	@Override
	public CommentDto get(Integer commentId) {
		// TODO Auto-generated method stub
		Optional<CommentEntity> result = commentRepository.findById(commentId);
		CommentEntity comment = result.orElseThrow();
		
		return toDto(comment);
	}

	@Override
	public Integer create(CommentDto commentDto) {
		Optional<MemberEntity> member = memberRepository.findById(commentDto.getMemberId());
		MemberEntity foundMember = member.orElseThrow();
		System.out.println("[[CommentService]] foundMember: " + foundMember.toString());
		
		Optional<PostEntity> post = postRepository.findById(commentDto.getPostId());
		PostEntity foundPost = post.orElseThrow();
		System.out.println("[[CommentService]] foundPost: " + foundPost.toString());
		
		CommentEntity commentEntity = toEntity(commentDto, foundMember, foundPost);
		System.out.println("[[CommentService]] commentEntity: " + commentEntity.getContent());
		
		CommentEntity result = commentRepository.save(commentEntity);
		System.out.println("[[CommentService]] SAVE DB");
		
		return result.getCommentId();
	}

	@Override
	public void modify(CommentDto commentDto) {
		// TODO Auto-generated method stub
		//어쩌지..
	}

	@Override
	public void remove(Integer postId, Integer commentId) {
	    Optional<PostEntity> postOptional = postRepository.findById(postId);
	    PostEntity post = postOptional.orElseThrow(() -> new RuntimeException("Post not found")); // 게시물이 없을 경우 예외 처리
	    
	    Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);
	    CommentEntity comment = commentOptional.orElseThrow(() -> new RuntimeException("Comment not found")); // 댓글이 없을 경우 예외 처리

	    // 댓글이 해당 게시물에 속하는지 확인
	    if (!comment.getPost().equals(post)) {
	        throw new RuntimeException("Comment does not belong to the specified post"); // 댓글이 해당 게시물에 속하지 않음
	    }

	    commentRepository.delete(comment); // 댓글 삭제
	    System.out.println("[[CommentService]] Comment deleted: " + commentId);
	}


//	@Override
//	public List<CommentDto> getAllComments(Integer postId) {
//		 List<CommentEntity> comments = commentRepository.findByPost_PostId(postId); // 포스트 ID로 댓글 조회
//		    return comments.stream()
//		            .map(this::toDto) // CommentEntity를 CommentDto로 변환
//		            .collect(Collectors.toList()); // 리스트로 수집
//	}
	
	@Override
    public PageResponseDto<CommentDto> getCommentsByPostId(Integer postId, PageRequestDto pageRequestDto) {
        int page = pageRequestDto.getPage() - 1; // JPA는 페이지 번호가 0부터 시작
        int size = pageRequestDto.getSize();
        
        Page<CommentEntity> commentEntities = commentRepository.findByPost_PostId(postId, PageRequest.of(page, size));
        
        // CommentEntity를 CommentDto로 변환
        List<CommentDto> commentDtos = commentEntities.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        // PageResponseDto로 변환하여 반환
        return PageResponseDto.<CommentDto>withAll()
                .dtoList(commentDtos)
                .pageRequestDto(pageRequestDto)
                .total(commentEntities.getTotalElements())
                .build();
    }
	
	// CommentEntity -> CommentDto 변환
	public CommentDto toDto(CommentEntity commentEntity) {
	    return CommentDto.builder()
	        .commentId(commentEntity.getCommentId())
	        .memberId(commentEntity.getMember().getMember_id())  // MemberEntity에서 memberId 추출
	        .postId(commentEntity.getPost().getPostId())        // PostEntity에서 postId 추출
	        .content(commentEntity.getContent())
	        .commentDate(commentEntity.getCommentDate())
	        .build();
	}
	// CommentDto -> CommentEntity 변환	
	public static CommentEntity toEntity(CommentDto commentDto, MemberEntity member, PostEntity post) {
	    return CommentEntity.builder()
	        .commentId(commentDto.getCommentId())
	        .member(member)          // MemberEntity는 외부에서 주입
	        .post(post)              // PostEntity도 외부에서 주입
	        .content(commentDto.getContent())
	        .commentDate(commentDto.getCommentDate())
	        .build();
	}

	@Override
	public List<CommentDto> getAllComments(Integer postId) {
		// TODO Auto-generated method stub
		return null;
	}
}
