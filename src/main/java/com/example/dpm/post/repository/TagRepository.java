package com.example.dpm.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.post.model.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Integer>{
	Optional<TagEntity> findByTagName(String name);
}
