package com.example.dpm.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.post.model.ImgEntity;
import com.example.dpm.post.model.TagEntity;

public interface ImgRepository extends JpaRepository<ImgEntity, Integer>{
	Optional<TagEntity> findByImgId(Integer imgId);
}
