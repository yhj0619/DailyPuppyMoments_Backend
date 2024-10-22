package com.example.dpm.puppy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.puppy.model.PuppyEntity;

public interface PuppyRepository extends JpaRepository<PuppyEntity, Integer>{

}
