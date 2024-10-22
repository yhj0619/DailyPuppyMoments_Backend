package com.example.dpm.mission_img.model;

import com.example.dpm.mission.model.MissionEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "mission_img")
public class MissionImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int missionImgId;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private MissionEntity mission; // Reference to Mission entity

    @Column(nullable = false)
    private String img; // Image URL
}
