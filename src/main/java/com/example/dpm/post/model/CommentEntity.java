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
@Table(name = "comment")
public class CommentEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate commentDate;
    
    @PrePersist // 엔티티가 생성될 때 호출
	public void prePersist() {
		this.commentDate = LocalDate.now(); // 현재 시간으로 초기화
	}
}
