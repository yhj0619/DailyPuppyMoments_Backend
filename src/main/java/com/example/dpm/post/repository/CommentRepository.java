package com.example.dpm.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.post.model.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{

	Page<CommentEntity> findByPost_PostId(Integer postId, Pageable pageable);

}
