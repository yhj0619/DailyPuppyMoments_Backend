package com.example.dpm.post.service;

import org.springframework.stereotype.Service;

import com.example.dpm.post.dto.MissionImgDto;
import com.example.dpm.post.model.MissionEntity;
import com.example.dpm.post.model.MissionImgEntity;

@Service
public class MissionService {
	 // Entity to DTO
    public static MissionImgDto toDto(MissionImgEntity missionImgEntity) {
        return MissionImgDto.builder()
                .missionImgId(missionImgEntity.getMissionImgId())
                .missionId(missionImgEntity.getMission().getMissionId())
                .img(missionImgEntity.getImg())
                .build();
    }

    // DTO to Entity
    public static MissionImgEntity toEntity(MissionImgDto missionImgDTO, MissionEntity mission) {
        return MissionImgEntity.builder()
                .missionImgId(missionImgDTO.getMissionImgId())
                .mission(mission)
                .img(missionImgDTO.getImg())
                .build();
    }
}
