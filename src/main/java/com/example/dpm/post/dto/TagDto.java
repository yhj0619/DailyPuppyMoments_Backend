package com.example.dpm.post.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.dpm.post.model.PostEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto {
    private Integer tagId; // 태그의 고유 ID
    private String name; // 태그 이름 추가 (예: "technology", "science")

//     PostDto와의 관계 (다대다 관계의 표현)
//    private List<PostDto> posts;
}
