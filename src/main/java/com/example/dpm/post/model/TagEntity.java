package com.example.dpm.post.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;

    @Column(nullable = false, unique = true)
    private String tagName; // 태그 이름, 예: "technology", "science"
    
    // 다대다 관계 설정
    @ManyToMany(mappedBy = "tags")
    private List<PostEntity> posts;
} 