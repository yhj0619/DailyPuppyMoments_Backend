package com.example.dpm.mission_img.service;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.mission.dto.MissionDto;
import com.example.dpm.mission.model.MissionEntity;

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
