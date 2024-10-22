package com.example.dpm.post.service;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.post.dto.PuppyDto;
import com.example.dpm.post.model.PuppyEntity;

@Service
public class PuppyService {
	 // Entity to DTO
    public static PuppyDto toDto(PuppyEntity puppyEntity) {
        return PuppyDto.builder()
                .puppyId(puppyEntity.getPuppyId())
                .memberId(puppyEntity.getMember().getMember_id()) // Extract memberId
                .name(puppyEntity.getName())
                .birth(puppyEntity.getBirth())
                .weight(puppyEntity.getWeight())
                .build();
    }

    // DTO to Entity
    public static PuppyEntity toEntity(PuppyDto puppyDTO, MemberEntity member) {
        return PuppyEntity.builder()
                .puppyId(puppyDTO.getPuppyId())
                .member(member) // MemberEntity is required here
                .name(puppyDTO.getName())
                .birth(puppyDTO.getBirth())
                .weight(puppyDTO.getWeight())
                .build();
    }
}
