package com.example.dpm.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.post.model.PuppyEntity;

public interface PuppyRepository extends JpaRepository<PuppyEntity, Integer>{

}
