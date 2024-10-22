package com.example.dpm.mission.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.mission.model.MissionEntity;

public interface MissionRepository extends JpaRepository<MissionEntity, Integer>{

}
