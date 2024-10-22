package com.example.dpm.post.service;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.post.dto.PostDto;
import com.example.dpm.post.model.PostEntity;

@Service
public class PostService {
	 // Entity to DTO
    public static PostDto toDto(PostEntity postEntity) {
        return PostDto.builder()
                .postId(postEntity.getPostId())
                .memberId(postEntity.getMember().getMember_id()) // Extract memberId
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .postDate(postEntity.getPostDate())
                .img(postEntity.getImg())
                .tagId(postEntity.getTagId())
                .emoji(postEntity.getEmoji())
                .totalLikeHeart(postEntity.getTotalLikeHeart())
                .myLikeHeart(postEntity.isMyLikeHeart())
                .build();
    }

    // DTO to Entity
    public static PostEntity toEntity(PostDto postDTO, MemberEntity member) {
        return PostEntity.builder()
                .postId(postDTO.getPostId())
                .member(member) // You need to pass MemberEntity separately
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .postDate(postDTO.getPostDate())
                .img(postDTO.getImg())
                .tagId(postDTO.getTagId())
                .emoji(postDTO.getEmoji())
                .totalLikeHeart(postDTO.getTotalLikeHeart())
                .myLikeHeart(postDTO.isMyLikeHeart())
                .build();
    }
}
