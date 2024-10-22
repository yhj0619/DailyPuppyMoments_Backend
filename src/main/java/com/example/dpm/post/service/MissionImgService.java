package com.example.dpm.post.service;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.post.dto.MissionDto;
import com.example.dpm.post.model.MissionEntity;

@Service
public class MissionImgService {
	 // Entity to DTO
    public static MissionDto toDto(MissionEntity missionEntity) {
        return MissionDto.builder()
                .missionId(missionEntity.getMissionId())
                .memberId(missionEntity.getMember().getMember_id())
                .status(missionEntity.isStatus())
                .missionDate(missionEntity.getMissionDate())
                .build();
    }

    // DTO to Entity
    public static MissionEntity toEntity(MissionDto missionDTO, MemberEntity member) {
        return MissionEntity.builder()
                .missionId(missionDTO.getMissionId())
                .member(member)
                .status(missionDTO.isStatus())
                .missionDate(missionDTO.getMissionDate())
                .build();
    }
}
