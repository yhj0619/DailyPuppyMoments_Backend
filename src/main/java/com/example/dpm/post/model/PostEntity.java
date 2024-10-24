package com.example.dpm.post.model;

import java.time.LocalDate;
import java.util.List;

import com.example.dpm.member.model.MemberEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
public class PostEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private MemberEntity member; // Reference to Member entity

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private LocalDate postDate;

	@Column(nullable = false)
	private String img;

	@Column(nullable = false)
	private String emoji;

	@Column(nullable = false)
	private Integer totalLikeHeart = 0; // 총 좋아요 수

	@Column(nullable = false)
	private boolean myLikeHeart = false; // 내가 좋아요를 눌렀는지 여부

	// 다대다 관계 설정
	@ManyToMany
	@JoinTable(name = "post_tags", // 중간 테이블 이름
			joinColumns = @JoinColumn(name = "post_id"), // 게시글의 외래 키
			inverseJoinColumns = @JoinColumn(name = "tag_id") // 태그의 외래 키
	)
	private List<TagEntity> tags; //post하나에 여러 태그. 태그하나에 여러 태그.
	
	// 댓글과의 일대다 관계 설정
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CommentEntity> comments;
    
    
    // 좋아요 상태 토글 메서드
    public void toggleLikeHeart() {
        if (this.myLikeHeart) {
            this.myLikeHeart = false;
            this.totalLikeHeart = Math.max(0, this.totalLikeHeart - 1); // 0 이하로 내려가지 않도록 처리
        } else {
            this.myLikeHeart = true;
            this.totalLikeHeart += 1;
        }
    }
	
	@PrePersist // 엔티티가 생성될 때 호출
	public void prePersist() {
		this.postDate = LocalDate.now(); // 현재 시간으로 초기화
	}
}
