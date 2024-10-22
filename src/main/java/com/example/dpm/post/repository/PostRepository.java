package com.example.dpm.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.post.model.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer>{

}
