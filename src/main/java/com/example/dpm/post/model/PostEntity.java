package com.example.dpm.post.model;
import java.time.LocalDate;

import com.example.dpm.member.model.MemberEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

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
    private int tagId;

    @Column(nullable = false)
    private String emoji;

    private int totalLikeHeart; // Total likes

    private boolean myLikeHeart; // If the user liked the post
}
