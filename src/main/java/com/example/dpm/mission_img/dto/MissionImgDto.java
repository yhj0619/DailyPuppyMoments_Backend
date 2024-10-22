package com.example.dpm.mission_img.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionImgDto {
    private int missionImgId;
    private int missionId; // Mission reference by missionId
    private String img; // Image URL
}
